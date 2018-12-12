package com.pelletier;

import cesiumlanguagewriter.CesiumOutputStream;
import cesiumlanguagewriter.CesiumStreamWriter;
import com.pelletier.czml.util.ColorGenerator;
import com.pelletier.czml.writers.billboard.BillboardWriter;
import com.pelletier.czml.writers.billboard.DefaultBillboardInfoProvider;
import com.pelletier.czml.writers.label.DefaultLabelInfoProvider;
import com.pelletier.czml.writers.label.LabelWriter;
import com.pelletier.czml.writers.packet.DocumentPacketInfoProvider;
import com.pelletier.czml.writers.packet.DocumentPacketWriter;
import com.pelletier.czml.writers.packet.SatellitePacketWriter;
import com.pelletier.czml.writers.path.SatellitePathWriter;
import com.pelletier.czml.writers.path.TlePathInfoProvider;
import com.pelletier.czml.writers.position.SatellitePositionWriter;
import com.pelletier.czml.writers.position.TlePositionProvider;
import gov.sandia.phoenix.elements.tle.TLE;
import gov.sandia.phoenix.propagators.sgp4.WGS84;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import scala.Some;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.exit;

@SpringBootApplication
public class JavaTleToCzml implements CommandLineRunner {


    public static void main(String[] args) throws Exception{
        SpringApplication app = new SpringApplication(JavaTleToCzml.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.setLogStartupInfo(false);
        app.run(args);
    }


    @Override
    public void run(String... args) throws Exception {

        String[] colors = {
                "#39add1", // light blue
                "#3079ab", // dark blue
                "#c25975", // mauve
                "#e15258", // red
                "#f9845b", // orange
                "#838cc7", // lavender
                "#7d669e", // purple
                "#53bbb4", // aqua
                "#51b46d", // green
                "#e0ab18", // mustard
                "#637a91", // dark gray
                "#f092b0", // pink
                "#b7c0c7"  // light gray
        };


        String fileName = "tles.txt";

        if(args.length > 0){
            fileName = args[0];
        }


        //read file into stream, try-with-resources
        List<String> tleFileLines = Files.lines(Paths.get(fileName)).collect(Collectors.toList());
        StringWriter stringWriter = new StringWriter();

        CesiumOutputStream cesiumOutputStream = new CesiumOutputStream(stringWriter);
        cesiumOutputStream.setPrettyFormatting(true);
        //writeEndSequence writes a '['
        cesiumOutputStream.writeStartSequence();

        CesiumStreamWriter cesiumStreamWriter = new CesiumStreamWriter();


        //WRITE REQUIRED CZML DOCUMENT PACKET
        DocumentPacketWriter documentPacketWriter = new DocumentPacketWriter();
        documentPacketWriter.setPacketInfoProvider(new DocumentPacketInfoProvider());
        documentPacketWriter.writePacket(cesiumStreamWriter, cesiumOutputStream);

        //generate values for the next 24 hours
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + (1000 * 60 * 60 * 24));


        int i = 0;
        while(i < tleFileLines.size()){

            String packetId = String.valueOf(i + 1);
            String name = tleFileLines.get(i);
            String tleLine1 = tleFileLines.get(i + 1);
            String tleLine2 = tleFileLines.get(i + 2);

            TLE tle = new TLE(new Some<>(name), tleLine1, tleLine2, WGS84.getInstance());

            //CONFIGURE AND WRITE SATELLITE PACKET

            //Configure Label Writer
            LabelWriter labelWriter = new LabelWriter();
            DefaultLabelInfoProvider defaultLabelInfoProvider = new DefaultLabelInfoProvider();
            defaultLabelInfoProvider.setTextProperty(name);
            labelWriter.setLabelInfoProvider(defaultLabelInfoProvider);

            //Configure Billboard Writer
            BillboardWriter billboardWriter = new BillboardWriter();
            DefaultBillboardInfoProvider defaultBillboardInfoProvider = new DefaultBillboardInfoProvider();
            if(i > 0){
                defaultBillboardInfoProvider.setImageReferenceString("1#billboard.image");
            }
            billboardWriter.setBillboardInfoProvider(defaultBillboardInfoProvider);

            //Configure Position Writer
            SatellitePositionWriter satellitePositionWriter = new SatellitePositionWriter();
            TlePositionProvider tlePositionProvider = new TlePositionProvider();
            tlePositionProvider.setTle(tle);
            tlePositionProvider.setStartDate(startDate);
            tlePositionProvider.setEndDate(endDate);
            tlePositionProvider.setTimeStep(300);
            satellitePositionWriter.setPositionProvider(tlePositionProvider);

            //Configure Path Writer
            SatellitePathWriter satelliteOrbitWriter = new SatellitePathWriter();
            TlePathInfoProvider tlePathInfoProvider = new TlePathInfoProvider();
            tlePathInfoProvider.setTle(tle);
            tlePathInfoProvider.setStartDate(startDate);
            tlePathInfoProvider.setWidthProperty(1.0);
            tlePathInfoProvider.setResolutionProperty(120.0);
            tlePathInfoProvider.setColorProperty(ColorGenerator.getRandomColor());
            tlePathInfoProvider.setShowProperty(true);
            satelliteOrbitWriter.setPathInfoProvider(tlePathInfoProvider);


            //Write Satellite Packet
            SatellitePacketWriter satellitePacketWriter = new SatellitePacketWriter();
            satellitePacketWriter.setId(packetId);
            satellitePacketWriter.setLabelWriter(labelWriter);
            satellitePacketWriter.setBillboardWriter(billboardWriter);
            satellitePacketWriter.setSatellitePositionWriter(satellitePositionWriter);
            satellitePacketWriter.setSatellitePathWriter(satelliteOrbitWriter);
            satellitePacketWriter.writePacket(cesiumStreamWriter, cesiumOutputStream);

            i += 3;
        }


        cesiumOutputStream.writeEndSequence();

        try (PrintWriter out = new PrintWriter("satellites.czml")) {
            out.println(stringWriter.toString());
        }

        exit(0);
    }


}
