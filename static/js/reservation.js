Vue.component("reservation", {
	data: function () {
	    return {
	    	
	    	apartment:{},
	    	numberOfDays:1,
	    	textComment:'',
	    	available:'DONT_KNOW',
	    	selectedDate: '',
	    	unavailableDays : {},
	    	price:0
	    }
	},
	template: ` 
<div>

		<div>
		
			<table class="table">
			
				<tr>
					
						<td>
						
									<table>
									<tr>
										<td>Izaberite datum dolaska: </td>
									  	<td><vuejs-datepicker  v-model="selectedDate" ></vuejs-datepicker></td>
									</tr>
									<tr>
										<td>Unesite broj dana: </td>
									  	<td><input class="input" placeholder="Unesite broj dana" type="number" min="1" v-model="numberOfDays" /></td>
									</tr>
									<tr>
										<td><button class="buttonBlue" v-on:click="proveraDostupnosti">Proverite dostupnost</button></td>
									</tr>
									</table>
							 
									
									<div v-bind:hidden="available!='FALSE'" v-if="selectedDate != ''">
								      <h4 style="color:red">U periodu od {{selectedDate | dateFormat('DD.MM.YYYY')}} do {{(selectedDate.getTime() + (parseInt(this.numberOfDays)-1)*24*60*60*1000) | dateFormat('DD.MM.YYYY')}} nema slobodnih termina!</h4>
							        </div>
						
						</td>
				
				
						<td>
						
						 <div v-bind:hidden="available!='TRUE'" v-if="selectedDate != ''">
								<h4>Za izabrani period postoji slobodan termin!</h4>
							<table style="width=50%">
								<tr>
									<td>Datum od:</td>
									<td >{{selectedDate | dateFormat('DD.MM.YYYY')}}</td>
								</tr>
								
								<tr>
									<td>Datum do:</td>
									<td>{{(selectedDate.getTime() + (parseInt(this.numberOfDays)-1)*24*60*60*1000) | dateFormat('DD.MM.YYYY')}}</td>
								</tr>
			
								<tr>
									<td style="color:red">Ukupna cena:</td>
									<td style="color:red">{{sumPrice() }} dinara</td>
								</tr>
								
								<tr>
									<td>Vreme ulaska u apartman:</td>
									<td>{{apartment.checkInTime}}</td>
								</tr>
								
								<tr>
									<td>Vreme izlaska iz apartmana:</td>
									<td>{{apartment.checkOutTime}}</td>
								</tr>
								
								<tr>
									<td colspan="2"><textarea class="inputComment"   placeholder="Unesite poruku za domacina"  cols="80" rows="15" v-model="textComment"></textarea></td>
								</tr>
								
								<tr>
									<td><button class="buttonBlue" v-on:click="reservation">Rezervisi</button></td>
								</tr>
						</table>
					</div>
						
						
				</td>
				
				
				</tr>
			
			
			
			</table>
		
		
		
		</div>







 
</div>		  
`, components : { 
		vuejsDatepicker
	},
	
	created (){
		axios
  		.get("/users/log/test")
  		.then(response => {
  			if(response.data == null){
  	      		  window.location.href = "#/login";
  			}
  		});
		
		axios
		.get('/apartment/occupiedDates/' + this.$route.query.id)
		.then(response => {
			let ocupiedDates = [];
			for(let d of response.data)
				ocupiedDates.push(new Date(d));
			this.unavailableDays["to"] = new Date(Date.now());
			this.unavailableDays["dates"] = ocupiedDates ;
			
		});
		
		axios
		.get('/apartment/occupiedRanges/' + this.$route.query.id)
		.then(response => {
			let ocupiedRanges = [];
			
			for(let d of response.data){
				if(d.dateTo != 0){
					ocupiedRanges.push({from : new Date(d.dateFrom), to : new Date(d.dateTo)});
				}else{
					this.unavailableDays["from"] = new Date(d.dateFrom);
				}
			}
			
			this.unavailableDays["ranges"] = ocupiedRanges;
		});
	},
	
	mounted () {
		
		
		axios
		.get('/apartment/' + this.$route.query.id)
		.then(response => (this.apartment = response.data));
				
		
	},
	methods : {
		proveraDostupnosti : function(){
			let numberOfDays = parseInt(this.numberOfDays);
			let selectedDays = [(new Date(this.selectedDate.getFullYear(),this.selectedDate.getMonth() , this.selectedDate.getDate())).getTime()];
			for (i = 1; i < numberOfDays; i++) {
				selectedDays.push((new Date(this.selectedDate.getFullYear(),this.selectedDate.getMonth() , this.selectedDate.getDate())).getTime() + 24*60*60*1000*i);
			}
			for(let unavailableDay of this.unavailableDays.dates){
				for(let day of selectedDays ){
					if(day === unavailableDay.getTime()){
						this.available = 'FALSE';
						return;
					}
				}
			}

			
			for(let dayFromRanges of this.unavailableDays["ranges"]){
				for(let dayFromSelectedDays of selectedDays ){
					if(dayFromSelectedDays > dayFromRanges.from.getTime() && dayFromSelectedDays < dayFromRanges.to.getTime()){
						this.available = 'FALSE';
						return;
					}else if(dayFromSelectedDays >= this.unavailableDays["from"].getTime() || dayFromSelectedDays <= this.unavailableDays["to"].getTime()){
						this.available = 'FALSE';
						return;
					}
				}
			}
			
			this.available = 'TRUE';
			
			
		},
		
		sumPrice : function(){
				
			let currentPrice=0;
			let numberOfDays = parseInt(this.numberOfDays);
			
			for (i = 1; i <= numberOfDays; i++) {
				currentPrice=currentPrice + this.apartment.priceForNight;
			}
			
			this.price=currentPrice;
			
			return currentPrice;
			
		},
		
		reservation : function(){

			let selectedDate = (new Date(this.selectedDate.getFullYear(),this.selectedDate.getMonth() , this.selectedDate.getDate())).getTime();
			let reservation = { appartment : this.apartment, startDate : selectedDate, daysForStay : parseInt(this.numberOfDays) - 1, price : this.sumPrice()
								, message : this.textComment, guest : null, status : 'created'};
			
			axios
			.post('/apartment/createReserveForSelectedApartment', JSON.stringify(reservation))
			.then(response => (window.location.href="#/reservations"));
		
		}
	
	
	},
    filters: {
    	dateFormat: function (value, format) {
    		var parsed = moment(value);
    		return parsed.format(format);
    	}
   	}
});