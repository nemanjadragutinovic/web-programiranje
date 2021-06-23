Vue.component("reservations", {
	data: function () {
	    return {
	        reservations: null,
	        selectedReservation : {},
	    	status : '',
	    	userType : null,
	    	guestUsername: '',
	    	sortValue:'',
	    	reservationStatus:'',
	    	searchedReservations: null,
	        showSearched:false,
	    }
	},
	template: ` 
<div>
<table style="margin-bottom: 50px;">
		<tr>
			<td><input class="searchSelect" placeholder="Korisnicko ime gosta" type="text"  v-model="guestUsername" name="guestUsername"/></td>
			<td>
			Sortiranje po ceni:
				<select class="searchSelect" name="sortValue" v-model="sortValue">
				   <option class="option" value=""></option>
				   <option class="option" value="rastuca">Cena rastuca</option>
				   <option class="option" value="opadajuca">Cena opadajuca</option>
				</select>
			</td>
			<td>
				Sortiranje po statusu:
				<select class="searchSelect" name="reservationStatus" v-model="reservationStatus">
				   <option class="option" value=""></option>
				   <option class="option" value="kreirano">Kreirano</option>
				   <option class="option" value="odbijeno">Odbijeno</option>
				   <option class="option" value="otkazano">Otkazano</option>
				   <option class="option" value="prihvaceno">Prihvaceno</option>
				   <option class="option" value="zavrseno">Zavrseno</option>
				</select>
			</td>
			<tr>
			<td><button class="button" v-on:click="search">Pretrazi</button></td>
			<td><button class="button" v-on:click="ponistipretragu">Ponisti pretragu</button></td>
			</tr>
					
		</tr>
</table>
<table>
<tr>
<td style="width:70%;vertical-align:top">
<table class="users" style="width:90%">
		<tr>
			<th>Tip</th>
			<th>Adresa</th>
			<th>Datum dolaska</th>
			<th>Datum povratka</th>
			<th>Cena boravka</th>
			<th>Username gosta</th>
			<th>Status</th>
		</tr>
		<tr v-bind:hidden="showSearched" v-for="r in reservations" v-on:click="selectReservation(r)" v-bind:class="{selected : selectedReservation.id===r.id}">
			<td>{{(r.appartment.type == 'apartment') ? 'Apartman' : 'Soba'}}</td>
			<td>{{r.appartment.location.adress.street + ' ' + r.appartment.location.adress.streetNumber + ', ' + r.appartment.location.adress.postNumber + ' ' + r.appartment.location.adress.city}}</td>
			<td>{{r.startDate | dateFormat('DD.MM.YYYY')}}</td>
			<td>{{r.startDate + r.daysForStay*24*60*60*1000 | dateFormat('DD.MM.YYYY')}}</td>
			<td>{{r.price}} dinara</td>
			<td>{{r.guest.username}}</td>
			<td v-if="r.status ==='created'">Kreirano</td>
			<td v-else-if="r.status ==='rejected'">Odbijeno</td>
			<td v-else-if="r.status ==='withdraw'">Otkazano</td>
			<td v-else-if="r.status ==='accepted'">Prihvaceno</td>
			<td v-else>Zavrseno</td>
		</tr>
		<tr v-bind:hidden="!showSearched" v-for="r in searchedReservations" v-on:click="selectReservation(r)" v-bind:class="{selected : selectedReservation.id===r.id}">
			<td>{{(r.appartment.type == 'apartment') ? 'Apartman' : 'Soba'}}</td>
			<td>{{r.appartment.location.adress.street + ' ' + r.appartment.location.adress.streetNumber + ', ' + r.appartment.location.adress.postNumber + ' ' + r.appartment.location.adress.city}}</td>
			<td>{{r.startDate | dateFormat('DD.MM.YYYY')}}</td>
			<td>{{r.startDate + r.daysForStay*24*60*60*1000 | dateFormat('DD.MM.YYYY')}}</td>
			<td>{{r.price}} dinara</td>
			<td>{{r.guest.username}}</td>
			<td v-if="r.status ==='created'">Kreirano</td>
			<td v-else-if="r.status ==='rejected'">Odbijeno</td>
			<td v-else-if="r.status ==='withdraw'">Otkazano</td>
			<td v-else-if="r.status ==='accepted'">Prihvaceno</td>
			<td v-else>Zavrseno</td>
		</tr>
</table>
<br>
<br>
<tr>
<table style=" background:rgb(224,224,224);">
			<tr>
				<td>Datum dolaska:</td>
				<td><vuejs-datepicker name="odlazak" type="text" v-model="selectedReservation.startDate" format="dd.MM.yyyy" :disabled="true"></vuejs-datepicker></td>
			</tr>
			<tr>
				<td>Datum povratka:</td>
				<td><vuejs-datepicker name="povratak" type="text" v-model="selectedReservation.startDate + selectedReservation.daysForStay*24*60*60*1000" format="dd.MM.yyyy" :disabled="true"></vuejs-datepicker></td>
			</tr>
			<tr>
				<td>Cena boravka:</td>
				<td><input name="cena" type="text" v-model="selectedReservation.price" disabled="true" /></td>
			</tr>
			<tr>
				<td>Status rezervacije:</td>
				<td v-if="selectedReservation.status ==='created'">Kreirano</td>
				<td v-else-if="selectedReservation.status ==='rejected'">Odbijeno</td>
				<td v-else-if="selectedReservation.status ==='withdraw'">Otkazano</td>
				<td v-else-if="selectedReservation.status ==='accepted'">Prihvaceno</td>
				<td v-else>Zavrseno</td>
			</tr>
			<tr>
				<td colspan="2" >Poruka:</td>
			</tr>
			<tr>
				<td colspan="2"><textarea name="stat" style="float:right" cols="70" rows="5" v-model="selectedReservation.message" disabled="true"></textarea></td>
			</tr>
			<tr>
			<td>
				
				<button class="buttonBlue" v-bind:hidden="userType=='ADMIN' || userType=='GUEST'" v-on:click="approveReservation">Prihvati</button>				
				<button class="buttonRed" v-bind:hidden="userType=='ADMIN' || userType=='GUEST'" v-on:click="rejectReservation">Odbij</button>
				<button class="buttonBlue" v-bind:hidden="userType=='ADMIN' || userType=='HOST'" v-on:click="withdrawReservation">Odustanak</button>
				<button class="buttonBlue" v-bind:hidden="userType=='ADMIN' || userType=='GUEST'" v-on:click="finishReservation">Zavrsi</button>
			</td>
			</tr>
		</table>
</tr>
</td>




</tr>
</table>
</div>			  
`,
	mounted () {
		axios
        .get('/users/log/test')
        .then(response => {
        	if(response.data != null){
        		if(response.data.userType == "Host")
        			this.userType='HOST';
        		else if(response.data.userType == "Administrator")
        			this.userType = 'ADMIN';
        		else
        			this.userType = 'GUEST';

        	}else{
	      		  window.location.href = "#/login";
        	}
        });
		
		axios
		.get('/apartment/get/reservations')
		.then(response => (this.reservations = response.data));
		
		
	},
	methods :{
		selectReservation : function(reservation){
			this.selectedReservation = reservation;
			if(reservation.status ==='created')
				this.status = 'Kreirano';
			else if(reservation.status ==='rejected')
				this.status = 'Odbijeno';
			else if(reservation.status ==='withdraw')
				this.status = 'Otkazano';
			else if(reservation.status ==='accepted')
				this.status = 'Prihvaceno';
			else
				this.status = 'Zavrseno';
		},
		withdrawReservation : function(){
			if(this.selectedReservation.status == 'created' || this.selectedReservation.status == 'accepted'){
				axios
				.put('/apartment/withdraw/' + this.selectedReservation.id)
				.then(response => {
					this.selectedReservation.status = 'withdraw';
					toast("Rezervacija otkazana!");	
				});
			}else{
				toast("Moguce je odustati od rezervacije samo ako je sa statusom 'Kreirano' i 'Prihvaceno'!");
			}
		},
		rejectReservation : function(){
			if(this.selectedReservation.status == 'created' || this.selectedReservation.status == 'accepted'){
				axios
				.put('/apartment/reject/' + this.selectedReservation.id)
				.then(response => {
					this.selectedReservation.status = 'rejected';
					toast("Rezervacija odbijena!");	
				});
			}else{
				toast("Moguce je samo odbiti rezervaciju sa statusom 'Kreirano' i 'Prihvaceno'!");
			}
		},
		approveReservation : function(){
			if(this.selectedReservation.status == 'created'){
				axios
				.put('/apartment/accept/' + this.selectedReservation.id)
				.then(response => {
					this.selectedReservation.status = 'accepted';
					toast("Rezervacija prihvacena!");	
				});
			}else{
				toast("Moguce je odobriti samo one rezervacije sa statusom 'Kreirano'!");	
			}
		},
		finishReservation : function(){
			if((this.selectedReservation.startDate + this.selectedReservation.daysForStay*24*60*60*1000) < Date.now() && this.selectedReservation.status == 'accepted'){
				axios
				.put('/apartment/finished/' + this.selectedReservation.id)
				.then(response => {
					this.selectedReservation.status = 'done';
					toast("Rezervacija zavrsena!");	
				});
			}else{
				toast("Prelazak u status 'Zavrseno' je moguce za one rezervacije koje su zavrsene!");		
			}
		},
		search : function(){
			if(this.guestUsername != '' || this.sortValue != '' || this.reservationStatus != '' ){
				axios
				.get('/reservation/search/parameters', {
				    params: {
				    	guestUsername: this.guestUsername,
				    	sortValue : this.sortValue,
				    	reservationStatus : this.reservationStatus,
				      }
				    })
				.then(response => {
					this.searchedReservations = response.data;
					this.showSearched = true;
				});
			}else{
				this.showSearched = false;
				this.searchedReservations = null;
			}
		},
		ponistipretragu: function(){
			this.searchedReservations = null;
			this.showSearched = false;
			this.reservationStatus = '';
			this.sortValue = '';
			this.guestUsername = '';
		}
		
	},
	components : { 
		vuejsDatepicker
	},
    filters: {
    	dateFormat: function (value, format) {
    		var parsed = moment(value);
    		return parsed.format(format);
    	}
   	}
});