package com.pelletier;

import static java.lang.System.exit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

import cesiumlanguagewriter.CesiumOutputStream;
import cesiumlanguagewriter.CesiumStreamWriter;
import gov.sandia.phoenix.elements.tle.TLE;
import gov.sandia.phoenix.propagators.sgp4.WGS84;
import scala.Some;

@SpringBootApplication
public class JavaTleToCzml implements CommandLineRunner {


    public static void main(String[] args) throws Exception{
        SpringApplication app = new SpringApplication(JavaTleToCzml.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.setLogStartupInfo(false);
        app.run(args);
    }


    /**
     * Generates CZML from list of strings of TLE lines.
     * @param tleLines
     * @return
     * @throws Exception
     */
	public static String tle2czml(List<String> tleLines) throws Exception {
		
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
        while(i < tleLines.size()){

            String packetId = String.valueOf(i + 1);
            String name = tleLines.get(i);
            String tleLine1 = tleLines.get(i + 1);
            String tleLine2 = tleLines.get(i + 2);

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
		
        return stringWriter.toString();
	}
    
    /**
     * Generates CZML from single string of concatenated TLE lines.
     * @param tles
     * @return
     * @throws Exception
     */
    public static String tle2czml(String tles) throws Exception {
    	
    	List<String> list = new ArrayList<String>();
    	
    	// create list of tleLines
    	String lines[] = tles.split("(\\\\r|\\\\n)");
    	
    	for (int i=0; i<lines.length; i++) {
    		
    		// clean up a little (remove any resident quote characters)
    		String line = lines[i].replace("\"", "");
    		list.add(line);
    	}
    	
    	return tle2czml(list);
    }

    @Override
    public void run(String... args) throws Exception {

        String fileName = "tles.txt";

        if(args.length > 0){
            fileName = args[0];
        }

        //read file into stream, try-with-resource
        List<String> tleFileLines = Files.lines(Paths.get(fileName)).collect(Collectors.toList());

        try (PrintWriter out = new PrintWriter("satellites.czml")) {
            out.println(tle2czml(tleFileLines));
        }

        exit(0);
    }
}
