package dao;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import Decoder.BASE64Decoder;
import beans.Amenity;
import beans.Apartment;
import beans.ApartmentStatus;
import beans.ApartmentType;
import beans.Guest;
import beans.Host;
import beans.Period;
import beans.Reservation;
import beans.ReservationStatus;


public class ApartmentDAO {
	private final String path = "./files/apartments.json";
	private static Gson g = new Gson();
	private UserDAO userDao;
	
	public ApartmentDAO(UserDAO userDao) {
		this.userDao = userDao;
	}
	
	public List<Apartment> GetAllFromFile() throws JsonSyntaxException, IOException{
		
		return g.fromJson((Files.readAllLines(Paths.get(path),Charset.defaultCharset()).size() == 0) ? "" : Files.readAllLines(Paths.get(path),Charset.defaultCharset()).get(0), new TypeToken<List<Apartment>>(){}.getType());
	}
	
	public List<Apartment> GetAll() throws JsonSyntaxException, IOException {
		List<Apartment> lista = GetAllFromFile();
		List<Apartment> retList = new ArrayList<Apartment>();
		
		
		if(lista!=null) {
			for(Apartment item : lista) {
				if(!item.isDeleted())
					retList.add(item);
			}
		}

		
		return retList;
	}
	
	
	
	public List<Apartment> GetAllApartmentForUser(int whatToGet, String username) throws JsonSyntaxException, IOException{		
		List<Apartment> retVal = new ArrayList<Apartment>();
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAll();
		
		if(apartments!=null) {
			for(Apartment a : apartments) {
				Host h = (Host) userDao.get(a.getHost().getUsername());
					if(whatToGet == 0) {
						if(a.getStatus()==ApartmentStatus.active) {
							if(!h.isBlocked())
								retVal.add(a);
						}
					}else if(whatToGet == 1) {
						if(a.getHost().getUsername().equals(username)) {
							retVal.add(a);
						}
					}
					else {
						retVal.add(a);
					}
				}
		}
		
		
		return retVal;
	}
	
	
	public Apartment Create(Apartment apartment) throws JsonSyntaxException, IOException {
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
		apartment.setId(GetMaxID());
		if(apartments == null) {
			apartments = new ArrayList<Apartment>();
		}
		if(apartment.getDateForRenting().size() > 0)
			apartment.setFreeDateForRenting(setFreeDateFromPeriod(apartment.getDateForRenting().get(0)));
		
		apartment.setStatus(ApartmentStatus.inactive);
		
		List<String> lista = new ArrayList<String>();
		 
        int numberOfImages=0;
        for(String item : apartment.getPictures()) {
            numberOfImages++;
            String imageString = item.split(",")[1];
 
            BufferedImage image = null;
            byte[] imageByte;
 
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
 
            String imageName= apartment.getId() + "-" + numberOfImages + ".png";
                       
            lista.add("pictures\\" + imageName);
           
            File outputfile = new File(System.getProperty("user.dir")+ "\\static\\pictures\\" + imageName);
            ImageIO.write(image, "png", outputfile);
        }
       
        apartment.setPictures(lista);
		
		apartments.add(apartment);
		SaveAll(apartments);
		return apartment;
	}
	
	private List<Long> setFreeDateFromPeriod(Period period) {
		List<Long> retVal = new ArrayList<Long>();
			
		long temp = period.getDateFrom();
		long endDate = period.getDateTo();

		while(temp <= endDate) {
			retVal.add(temp);
			temp += 24*60*60*1000;
		}
		return retVal;
	}

	public Apartment getApartment(String id) throws JsonSyntaxException, IOException {
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAll();
		if(apartments != null && id != null) {
			for(Apartment a : apartments) {
				if(a.getId() == Integer.parseInt(id)) {
					return a;
				}
			}
		}
		return null;
	}
	
	
	
	
	
	
	
	
	private int GetMaxIDForReservation() throws JsonSyntaxException, IOException  {
		int maxId = 0;
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
		for(Apartment a : apartments) {
			for(Reservation r : a.getReservations()) {
				if(r.getId() > maxId)
					maxId = r.getId();
			}
		}
		return ++maxId;
	}
	
	
	
	public List<Reservation> getAllReservations(int whatToGet, String username) throws JsonSyntaxException, IOException{
		List<Reservation> retVal = new ArrayList<Reservation>();
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
		
		for(Apartment a : apartments) {
			if(a.isDeleted()==false) {
							
			for(Reservation r : a.getReservations()) {
				if(whatToGet == 0) {
					if(r.getGuest().getUsername().equals(username)) {
						r.setAppartment(new Apartment(a.getId(),a.getType(),a.getNumberOfRoom(),a.getNumberOfGuest(),a.getLocation(), null, null, null,  null,0,"", "", null, null, null));
						retVal.add(r);
					}
				}else if(whatToGet == 1) {
					if(a.getHost().getUsername().equals(username)) {
						r.setAppartment(new Apartment(a.getId(),a.getType(),a.getNumberOfRoom(),a.getNumberOfGuest(),a.getLocation(), null, null,  null, null,0,"", "", null, null, null));
						retVal.add(r);
					}
				}
				else {
					r.setAppartment(new Apartment(a.getId(),a.getType(),a.getNumberOfRoom(),a.getNumberOfGuest(),a.getLocation(), null, null, null, null,0,"", "", null, null, null));
					retVal.add(r);
				}
			}
			}
		}
			
		return retVal;
	}
	
	
	
	
	
	

	private int GetMaxID() throws JsonSyntaxException, IOException {
		int maxId = 0;
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
		if(apartments != null) {
			for(Apartment a : apartments) {
				if(a.getId() > maxId)
					maxId = a.getId();
			}
		}
		return ++maxId;
	}
	
	public void SaveAll(Collection<Apartment> apartments) throws JsonIOException, IOException{
	    Writer writer = new FileWriter(path);
		g.toJson(apartments, writer);
	    writer.close();
	}
	
	public List<Apartment> searchApartments(String location, String dateFrom, String dateTo, String numberOfGuest,String minRoom, String maxRoom, String minPrice, String maxPrice, String sortValue, String type, String apartmentStatus,List<Amenity> amenities,int whatToGet , String username) throws JsonSyntaxException, IOException{
			
		
		
			ArrayList<Apartment> list = (ArrayList<Apartment>) GetAllApartmentForUser(whatToGet, username);
			List<Apartment> retVal = new ArrayList<Apartment>();
						
			ApartmentType tip;
			if(type.equals("soba"))
				tip = ApartmentType.room;
			else
				tip = ApartmentType.apartment;
			
			ApartmentStatus status;
			if(apartmentStatus.equals("aktivan"))
				status = ApartmentStatus.active;
			else
				status = ApartmentStatus.inactive;
	
			//datefrom//dateto
			for(Apartment item : list) {
				if((!location.isEmpty() ? item.getLocation().getAdress().getCity().toLowerCase().contains(location.toLowerCase()) : true) 
						&& (!numberOfGuest.isEmpty()? item.getNumberOfGuest()>=Integer.parseInt(numberOfGuest):true)
						&& ((!minRoom.isEmpty())? (item.getNumberOfRoom()>=Integer.parseInt(minRoom)) :true)
						&&((!maxRoom.isEmpty())? (item.getNumberOfRoom()<=Integer.parseInt(maxRoom)): true)
						&& ((!minPrice.isEmpty())? (item.getPriceForNight()>=Integer.parseInt(minPrice)) :true)
						&&((!maxPrice.isEmpty())? (item.getPriceForNight()<=Integer.parseInt(maxPrice)): true)
						&&((!apartmentStatus.isEmpty())? (item.getStatus()==status): true)
						&&((!type.isEmpty())? (item.getType()==tip): true)) {
					if(!dateFrom.isEmpty() || !dateTo.isEmpty()){
						for(long datum : item.getFreeDateForRenting()) {
							if(((!dateFrom.isEmpty())? datum >= Long.parseLong(dateFrom) : true) && ((!dateTo.isEmpty()) ? datum <= Long.parseLong(dateTo) : true)) {
									if(amenities!=null) {
										if(whatToGet==1) {
											if(item.getStatus()==ApartmentStatus.active)
												if(uporediListe(item.getAmenities(), amenities))
													retVal.add(item);
										}
										else {
											if(uporediListe(item.getAmenities(), amenities))
												retVal.add(item);
										}
									}else {
										if(whatToGet==1) {
											if(item.getStatus()==ApartmentStatus.active)
													retVal.add(item);
										}else {
											retVal.add(item);
										}
									}
									
									break;
							}
						}
					}else {
						if(amenities!=null) {
							if(whatToGet==1) {
								if(item.getStatus()==ApartmentStatus.active)
									if(uporediListe(item.getAmenities(), amenities))
										retVal.add(item);
							}
							else {
								if(uporediListe(item.getAmenities(), amenities))
									retVal.add(item);
							}
						}else {
							if(whatToGet==1) {
								if(item.getStatus()==ApartmentStatus.active)
										retVal.add(item);
							}else {
								retVal.add(item);
							}
						}
					}
					
				}
						

			}	
			
			

						
			if(sortValue.equals("rastuca")) {
				Collections.sort(retVal, new Comparator<Apartment>() {
					@Override
					public int compare(Apartment o1, Apartment o2) {
						// TODO Auto-generated method stub
						return (int)(o1.getPriceForNight() - o2.getPriceForNight());
					}
				});	
			}else if(sortValue.equals("opadajuca")) {
				Collections.sort(retVal, new Comparator<Apartment>() {
					@Override
					public int compare(Apartment o1, Apartment o2) {
						// TODO Auto-generated method stub
						return (int)(o2.getPriceForNight() - o1.getPriceForNight());
					}
				});	
			}
			
			return retVal;
	
		}
	
	private boolean uporediListe(List<Amenity> listaApartmana,List<Amenity> listaPretrage){
		
		
		for(Amenity itemPretrage : listaPretrage) {
			boolean postoji=false;
			for(Amenity itemApartmana : listaApartmana) {
				if(itemApartmana.getId()==itemPretrage.getId()) {
					postoji=true;
				}
			}
			if(!postoji) {
				return false;
			}
			
		}
		
		return true;
	}
	
	public List<Reservation> searchReservation(String questUsername, String sortValue, String reservationStatus, int whatToGet , String username) throws JsonSyntaxException, IOException{
		
		ArrayList<Reservation> list = (ArrayList<Reservation>) getAllReservations(whatToGet,username);
		List<Reservation> retVal = new ArrayList<Reservation>();
		
		ReservationStatus status;
		if(reservationStatus.equals("kreirano"))
			status = ReservationStatus.created;
		else if(reservationStatus.equals("odbijeno"))
			status = ReservationStatus.rejected;
		else if(reservationStatus.equals("otkazano"))
			status = ReservationStatus.withdraw;
		else if(reservationStatus.equals("prihvaceno"))
			status = ReservationStatus.accepted;
		else 
			status = ReservationStatus.done;

		for(Reservation item : list) {
			if((!reservationStatus.isEmpty()? item.getStatus()==status : true)
					&& (!questUsername.isEmpty()? item.getGuest().getUsername().equals(questUsername) : true))
				retVal.add(item);
		}	
		
		if(sortValue.equals("rastuca")) {
			Collections.sort(retVal, new Comparator<Reservation>() {
				@Override
				public int compare(Reservation o1, Reservation o2) {
					// TODO Auto-generated method stub
					return (int)(o1.getPrice() - o2.getPrice());
				}
			});	
		}else if(sortValue.equals("opadajuca")) {
			Collections.sort(retVal, new Comparator<Reservation>() {
				@Override
				public int compare(Reservation o1, Reservation o2) {
					// TODO Auto-generated method stub
					return (int)(o2.getPrice() - o1.getPrice());
				}
			});	
		}
		
		
		
		return retVal;

	}


	
	
	
	public void deleteAllAmenities(int id) throws JsonSyntaxException, IOException {
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
		for(Apartment a : apartments) {
			for(Amenity am : a.getAmenities()) {
				if(am.getId() == id) {
					List<Amenity> amenities = a.getAmenities();
					amenities.remove(am);
					a.setAmenities(amenities);
					break;
				}
			}
		}
		SaveAll(apartments);
	}
	
	public void updateAllAmenities(Amenity amenity) throws JsonSyntaxException, IOException {
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
		for(Apartment a : apartments) {
			for(Amenity am : a.getAmenities()) {
				if(am.getId() == amenity.getId()) {
					List<Amenity> amenities = a.getAmenities();
					amenities.remove(am);
					amenities.add(amenity);
					a.setAmenities(amenities);
					break;
				}
			}
		}
		SaveAll(apartments);
	}
	

	
	public List<Period> getOccupiedRanges(String id) throws JsonSyntaxException, IOException{
		Apartment apartment = getApartment(id);
		List<Period> ocupiedRangesList  = new ArrayList<Period>();

		List<Period> sortedPeriodsForThisApartment = sortApartmentPeriods(apartment.getDateForRenting());
		
		Date date = new Date();
	    @SuppressWarnings("deprecation")
		Long today = (new Date(date.getYear(),date.getMonth(), date.getDate())).getTime();
		
		for(Period period : sortedPeriodsForThisApartment) {
			ocupiedRangesList.add(new Period(today, period.getDateFrom() - 24*60*60*1000));
			
			today = period.getDateTo() + 24*60*60*1000;
		}
		
		
		ocupiedRangesList.add(new Period(today, 0));
		
		return ocupiedRangesList;
	}
	
	
	
	public List<Long> getOccupiedDates(String id) throws JsonSyntaxException, IOException{
		List<Long> ocupiedDaysList = new ArrayList<Long>();
		if(id != null) {
			Apartment apartment = getApartment(id);
			for(Period p : apartment.getDateForRenting()) {
				Date dateFrom = new Date(p.getDateFrom());
				Date dateTo = new Date(p.getDateTo());
				
				
				Calendar c = Calendar.getInstance(); 
				while(dateFrom.compareTo(dateTo) <= 0) {
					if(!apartment.getFreeDateForRenting().contains(dateFrom.getTime())) {
						ocupiedDaysList.add(dateFrom.getTime());
					}
					c.setTime(dateFrom); 
					c.add(Calendar.DAY_OF_YEAR, 1);
					dateFrom = c.getTime();
				}
			}
		}
		return ocupiedDaysList;
	}
	
	
	
	
	private List<Period> sortApartmentPeriods(List<Period> listPeriodsForSort){
		List<Period> sortedPeriods = new ArrayList<Period>();
		Date date = new Date();
	    @SuppressWarnings("deprecation")
		Date today = new Date(date.getYear(),date.getMonth(), date.getDate());
		for(Period periodInListForSort : listPeriodsForSort) { 
			if(periodInListForSort.getDateFrom() >= today.getTime()) { 
				int i = 0;
				boolean added = false;
				for(Period periodInSortedList : sortedPeriods) {  
					if(periodInSortedList.getDateFrom() < periodInListForSort.getDateFrom()) { 
						sortedPeriods.add(i, periodInListForSort);
						added = true;
						break;
					}
					
					i++;
				}		
				if(!added)
					sortedPeriods.add(periodInListForSort);
			}
		}	
		return sortedPeriods;
	}
	
	
	public boolean createReserveForSelectedApartment(Reservation reservation) throws JsonSyntaxException, IOException {
		
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
	
		Guest guest = (Guest) userDao.get(reservation.getGuest().getUsername()); 
		
		boolean retVal = false;		
		
		for(Apartment apartment : apartments) { 
			if( apartment.getId() == reservation.getAppartment().getId()) {  
				List<Reservation> temp =  apartment.getReservations();  
				
				
				if(temp == null)
					temp = new ArrayList<Reservation>();
				
				reservation.setId(GetMaxIDForReservation());				
				reservation.setAppartment(null); 
				temp.add(reservation); 
				apartment.setReservations(temp); 
				apartment.setFreeDateForRenting(setFreeDaysForReservation(apartment.getFreeDateForRenting(),reservation));
				retVal = true;
				break;
			}
		}
		SaveAll(apartments);
		
				
		userDao.Update(guest);
		
		return retVal;
	}
	
	
	
	private List<Long> setFreeDaysForReservation(List<Long> freeDateForReservation, Reservation reservation) {
		
		List<Long> freeDaysForReservation = new ArrayList<Long>();
		List<Long> reservedDates = new ArrayList<Long>();
		long startDate = reservation.getStartDate();
		long endDate = reservation.getStartDate() + reservation.getDaysForStay()*24*60*60*1000;

		while(startDate <= endDate) {
			reservedDates .add(startDate);
			startDate += 24*60*60*1000;
		}
				
		for(long currentDate : freeDateForReservation) {
			if(!reservedDates .contains(currentDate )) {
				freeDaysForReservation .add(currentDate );
			}
		}
		
		return freeDaysForReservation ;
	}
	
	
	public Apartment updateCurrentApartment(Apartment apartment) throws JsonSyntaxException, IOException {
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
		
				
		
		for(Apartment a : apartments) {
			if(a.getId() == apartment.getId()) {
				apartments.remove(a);
				apartments.add(apartment);
				break;
			}
		}
		SaveAll(apartments);
		return apartment;
	}
	
	
	
	
	public boolean changeReservationStatus(String id, ReservationStatus status) throws JsonSyntaxException, IOException {
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
		boolean changed = false;
		for(Apartment a : apartments) {
			for(Reservation r : a.getReservations()) {
				if(r.getId() == Integer.parseInt(id)) {
					r.setStatus(status);
					if(status == ReservationStatus.rejected || status == ReservationStatus.withdraw)
						a.setFreeDateForRenting(AddDaysForRenting(a.getFreeDateForRenting(), r));
					changed = true;
					break;
				}
			}
			if(changed)
				break;
		}
		SaveAll(apartments);
		userDao.changeReservationStatus(id, status);
		return changed;
	}
	
	private List<Long> AddDaysForRenting(List<Long> dateForRenting, Reservation reservation) {
		long temp = reservation.getStartDate();
		long endDate = reservation.getStartDate() + reservation.getDaysForStay()*24*60*60*1000;

		while(temp <= endDate) {
			dateForRenting.add(temp);
			temp += 24*60*60*1000;
		}
		return dateForRenting;
	}
	
	
	
	public Apartment DeleteApartment(String id) throws JsonSyntaxException, IOException {
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
		Apartment retVal = null;
		for(Apartment a : apartments) {
			if(a.getId() == Integer.parseInt(id)) {
				a.setDeleted(true);
				retVal = a;
				break;
			}
		}
		SaveAll(apartments);
		
		
		
		
		
		
		return retVal;
		
		
		
	}
	
	
	public Apartment addNewPeriod(Apartment apartment) throws JsonSyntaxException, IOException {
		
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
		
						
		for(Apartment a : apartments) {
			if(a.getId() == apartment.getId()) {
				a.setDateForRenting(apartment.getDateForRenting());
				a.setFreeDateForRenting(apartment.getFreeDateForRenting());
				SaveAll(apartments);
				return a;
			}
		}
		
		return apartment;
	}
	
	
	
	


}
