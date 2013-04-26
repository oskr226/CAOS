package co.edu.uis.sistemas.simple.icasa;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;

import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
import fr.liglab.adele.icasa.device.light.BinaryLight;
import fr.liglab.adele.icasa.device.temperature.Cooler;
import fr.liglab.adele.icasa.device.temperature.Heater;
import fr.liglab.adele.icasa.device.temperature.Thermometer;


@Component(name="SimpleIcasaComponent")
@Instantiate
public class SimpleIcasaComponent implements DeviceListener{

	
	
	@Requires(id="lights")
	private BinaryLight[] lights;
	@Requires(id="heaters")
	private Heater[] heaters;
	@Requires (id="coolers")
	private Cooler[] coolers;
	@Requires(id="themometer")
	private Thermometer[] thermometers;
	
	private Thread modifyLightsThread;
	//private Thread modifyHeatersThread;
	//private Thread modifyCoolersThread;
	
	@Bind(id="lights")
	protected void bindLight(BinaryLight light) {
		System.out.println("A new light has been added to the platform " + light.getSerialNumber());
		//Registro el listener y cuando se vaya un des-registro.
		light.addListener(this);
	}
	
	  
	
	@Bind(id="heaters")
	protected void bindHeater(Heater heater){
		String location = (String) heater.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME);
		System.out.println("A new heater has been added to the platform " + heater.getSerialNumber());
	}
	
	@Bind(id="coolers")
	protected void bindCooler(Cooler cooler){
		
	}

	protected List<BinaryLight> getLights() {
		return Collections.unmodifiableList(Arrays.asList(lights));
	}
	
	protected List<Heater> getHeaters(){
		return Collections.unmodifiableList(Arrays.asList(heaters));
	}
	
	protected List<Cooler> getCoolers(){
		return Collections.unmodifiableList(Arrays.asList(coolers));
	}
	
	protected List<Thermometer> getThermometer(){
		return Collections.unmodifiableList(Arrays.asList(thermometers));
	}

	
	@Validate
	public void start() {
		modifyLightsThread = new Thread(new ModifyLigthsRunnable());
		modifyLightsThread.start();
		//modifyHeatersThread = new Thread(new ModifyHeatersRunnable());
		//modifyHeatersThread.start();
		
	}
	
	@Invalidate
	public void stop() throws InterruptedException {
		modifyLightsThread.interrupt();
		modifyLightsThread.join();
		//modifyHeatersThread.interrupt();
		//modifyHeatersThread.join();
	}

	
	class ModifyLigthsRunnable implements Runnable {

		public void run() {
						
			boolean running = true;
			
			boolean onOff = false;
			while (running) {
				onOff = !onOff;
				try {
					List<BinaryLight> lights = getLights();
					for (BinaryLight binaryLight : lights) {
						binaryLight.setPowerStatus(onOff);
					}
					Thread.sleep(1000);					
				} catch (InterruptedException e) {
					running = false;
				}
			}
			
		}
		
	}
	
	class ModifyHeatersRunnable implements Runnable {

		public void run() {
						
			boolean running = true;
			
			boolean onOff = false;
			while (running) {
				onOff = !onOff;
				try {
					List<Heater> heaters = getHeaters();
					for (Heater heater : heaters) {
						heater.setPowerLevel(0.2);
					}
					Thread.sleep(1000);					
				} catch (InterruptedException e) {
					running = false;
				}
			}
			
		}
		
	}
	
	//IMplementación de la interface	
	
	public void deviceAdded(GenericDevice arg0) {
		// TODO Auto-generated method stub TACHADO
		
	}

	public void devicePropertyAdded(GenericDevice arg0, String arg1) {
		// TODO Auto-generated method stub TACHADO
		
	}

	public void devicePropertyModified(GenericDevice device, String property, Object value) {		
		if(property == Thermometer.THERMOMETER_CURRENT_TEMPERATURE){
			List<Heater> Lheater = getHeaters();
			List<Cooler> Lcooler = getCoolers();
			
			for (Thermometer thermometer : getThermometer()) {
				if(thermometer.getTemperature() < 290){
					//recorrer todos los heaters encedenmos
					for (Heater heaterlocal : Lheater) {
						if(heaterlocal.LOCATION_PROPERTY_NAME == device.LOCATION_PROPERTY_NAME){
							heaterlocal.setPowerLevel(0.8);
						}						
					}
					
					//Recorremos los coolers cercanos y los apagamos
					for (Cooler coolerlocal : Lcooler) {
						if(coolerlocal.LOCATION_PROPERTY_NAME == device.LOCATION_PROPERTY_NAME){
							coolerlocal.setPowerLevel(0.0);
						}	
					}
					
				}
			}
			
		}
				
				
		// TODO Auto-generated method stub IMPLEMENTADO, me devuelve el dispositivo, la propiedad y el valor antiguos
		String id = (String) device.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME);
		System.out.println("Modificado " + id + "Propiedad " + property + "valor ");	
		
		
	}

	public void devicePropertyRemoved(GenericDevice arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	public void deviceRemoved(GenericDevice arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
