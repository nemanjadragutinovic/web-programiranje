Vue.component("home-page", {
	data: function () {
	    return {
	        apartments: null,
	        width:'50%',
	        location:'',
	        dateFrom:'',
	        dateTo:'',
	        numberOfGuest:'',
	        minRoom:'',
	        maxRoom:'',
	        minPrice:'',
	        maxPrice:'',
	        searchedApartments: null,
	        showSearched:false,
	        selectedAmenities: [],
	        sortValue:'',
	        visibleSearchBar: false,
	        amenities: null,
	        type: '',
	        apartmentStatus: '',
	        mod: 'default'
	    }
},
template: ` 
<div>
	
	
	
	<table>
		<tr v-bind:hidden="visibleSearchBar">
			<td><button class="button" v-on:click="openSearch">Otvori pretragu</button></td>	
		</tr>
		<tr v-bind:hidden="!visibleSearchBar" >
			<td><input class="searchInput" placeholder="Lokacija" type="text"  v-model="location" name="location"/></td>
			</tr>
			<br>
		<tr v-bind:hidden="!visibleSearchBar">
			<td>Datum od: <vuejs-datepicker v-model="dateFrom"></vuejs-datepicker></td>
			</tr>
			<br>
		<tr v-bind:hidden="!visibleSearchBar">
			<td>Datum do: <vuejs-datepicker v-model="dateTo"></vuejs-datepicker></td>
			</tr>
			<br>
		<tr v-bind:hidden="!visibleSearchBar">
			<td><input class="searchInput" placeholder="Broj gostiju" min=0 type="number" v-model="numberOfGuest" name="numberOfGuest"/></td>
		</tr>
		<br>
		<tr v-bind:hidden="!visibleSearchBar">
			<td><input class="searchInput" placeholder="Minimalno soba" min=0 type="number" v-model="minRoom" name="minRoom"/></td>
			<td><input class="searchInput" placeholder="Maksimalno soba" min=0 type="number" v-model="maxRoom" name="maxRoom"/></td>
			</tr>
			<tr v-bind:hidden="!visibleSearchBar">
			<td><input class="searchInput" placeholder="Minimalna cena" min=0 type="number" v-model="minPrice" name="minPrice"/></td>
			<td><input class="searchInput" placeholder="Maksimalna cena" min=0 type="number" v-model="maxPrice" name="maxPrice"/></td
		</tr>
		<br>
		<tr v-bind:hidden="!visibleSearchBar"><label>SADRZAJ</label></tr>
		<tr v-bind:hidden="!visibleSearchBar" v-for="(amenity, index) in amenities">
			<input type="checkbox" v-bind:value="amenity" v-model="selectedAmenities" :value="amenity"/>
          {{amenity.name}}
          
          </br>
        </tr>
		<tr v-bind:hidden="!visibleSearchBar">
			<td colspan="2">
				<select class="select" name="apartmentType" v-model="type">
				   <option class="option" value=""></option>
				   <option class="option" value="soba">Soba</option>
				   <option class="option" value="apartman">Apartman</option>
				</select>
				<select class="select" name="sort" v-model="sortValue">
				   <option class="option" value=""></option>
				   <option class="option" value="rastuca">Cena rastuca</option>
				   <option class="option" value="opadajuca">Cena opadajuca</option>
				</select>
				<select v-bind:hidden="mod!='ADMIN'" class="select" name="apartmentStatus" v-model="apartmentStatus">
				   <option class="option" value=""></option>
				   <option class="option" value="aktivan">Aktivan</option>
				   <option class="option" value="neaktivan">Neaktivan</option>
				</select>
			</td>	
		</tr>
		<br>
		<tr v-bind:hidden="!visibleSearchBar">
			<td><button class="button" v-on:click="ponistipretragu">Ponisti pretragu</button></td>		
			<td><button class="button" v-on:click="search">Pretrazi</button></td>
			</tr>	
	</table>
	
	
	<div v-bind:hidden="showSearched" class ="polozajOkvira" v-on:click="selectApartment(apartment.id)" v-bind:style="{ width: computedWidth }"   v-for="(apartment, index) in apartments">
          <table class ="tabelaPrikazaApartmanaUOkviru" >
                  <tr>
                      <td colspan="2" style="width: 100%;">
                        <img :src="apartment.pictures[0]"  height="400" width="700">
                      </td>
                  </tr>


                  <tr>
                      <td><label v-if="apartment.type === 'room'">Tip: Soba</label>
                      <label v-else>Tip: Apartman</label></td>

                  </tr>

                      <td><label >Lokacija: {{apartment.location.adress.city}} - {{apartment.location.adress.street}} {{apartment.location.adress.streetNumber}}</label></td> 
                  <tr>

                  </tr>
                  <tr>
                      <td><label>Broj gostiju: </label>
                      <label style="margin-left:10px;" >{{apartment.numberOfGuest}}</label></td>
                  </tr>
                  <tr>
                      <td><label>Cena:</label>
                      <label style="margin-left:10px;">{{apartment.priceForNight}} din po nocenju</label></td>
                  </tr>

          </table>
	</div>

	
	<div v-bind:hidden="!showSearched" 	v-on:click="selectApartment(apartment.id)" v-bind:style="{ width: computedWidth }" style = "margin-left:auto;margin-right:auto;" v-for="(apartment, index) in searchedApartments">
          <table class ="tabelaPrikazaApartmanaUOkviru" >
                  <tr>
                      <td colspan="2" style="width: 100%;">
                        <img :src="apartment.pictures[0]"  height="400" width="700">
                      </td>
                  </tr>


                  <tr>
                      <td><label v-if="apartment.type === 'room'">Tip: Soba</label>
                      <label v-else>Tip: Apartman</label></td>

                  </tr>

                      <td><label >Lokacija: {{apartment.location.adress.city}} - {{apartment.location.adress.street}} {{apartment.location.adress.streetNumber}}</label></td> 
                  <tr>

                  </tr>
                  <tr>
                      <td><label>Broj gostiju: </label>
                      <label style="margin-left:10px;" >{{apartment.numberOfGuest}}</label></td>
                  </tr>
                  <tr>
                      <td><label>Cena:</label>
                      <label style="margin-left:10px;">{{apartment.priceForNight}} din po nocenju</label></td>
                  </tr>

          </table>
	</div>
	
	
	
</div>		  	  
`, components : { 
		vuejsDatepicker
	},
	mounted () {
	    axios
	      .get('/apartmentsForUser')
	      .then(response => (this.apartments = response.data))
	      	
	   axios
	     .get('/amenities')
	     .then(response => (this.amenities = response.data))   

	   axios
        .get('/users/log/test')
        .then(response => {
        	if(response.data == null)
        		this.mod='USER';
        	else 
        		if(response.data.userType == "Guest")
        			this.mod='GUEST';
        		else if(response.data.userType == "Host")
        			this.mod='HOST';
        		else 
        			this.mod = 'ADMIN';
        })
	},
	computed: {
	    computedWidth: function () {
	      return this.width;
	    }
	  },
	  methods : {
			  openSearch : function(){
				  this.visibleSearchBar=true;
			  },
			search : function(){
				if(this.location != '' || this.dateFrom != '' || this.dateTo != '' || this.numberOfGuest != '' || this.minRoom != '' || this.maxRoom != '' || this.minPrice != '' || this.maxPrice != ''|| this.sortValue != '' || this.amenities !=null || this.apartmentStatus!='' || this.type!='' ){
					let datumOd = '';
					if(this.dateFrom != '')
						datumOd = (new Date(this.dateFrom.getFullYear(),this.dateFrom.getMonth() , this.dateFrom.getDate())).getTime();
					let datumDo='';
					if(this.dateTo != '')
						datumDo = (new Date(this.dateTo.getFullYear(),this.dateTo.getMonth() , this.dateTo.getDate())).getTime();
					axios
					.get('/apartments/search/parameters', {
					    params: {
					        location: this.location,
					        dateFrom : datumOd,
					        dateTo : datumDo,
					        numberOfGuest : this.numberOfGuest,
					        minRoom: this.minRoom,
					        maxRoom : this.maxRoom,
					        minPrice : this.minPrice,
					        maxPrice : this.maxPrice,
					        sortValue: this.sortValue,
					        type: this.type,
					        apartmentStatus: this.apartmentStatus,
					        amenities:JSON.stringify(this.selectedAmenities)
					      }
					    })
					.then(response => {
						this.searchedApartments = response.data;
						this.showSearched = true;
						this.visibleSearchBar=false;
					});
				}else{
					this.showSearched = false;
					this.searchedApartments = null;
					this.visibleSearchBar=false;
				}
			},
			ponistipretragu: function(){
				this.searchedApartments = null;
				this.showSearched = false;
				this.visibleSearchBar=false;
				this.dateFrom = '';
				this.dateTo = '';
				this.location = '';
				this.numberOfGuest = '';
				this.minRoom = '';
				this.maxRoom = '';
				this.minPrice = '';
				this.maxPrice ='';
				this.sortValue = '';
				this.type = '';
				this.apartmentStatus = '';
				this.selectedAmenities = [];
			},
	        selectApartment : function(id) {
	        	window.location.href = "#/apartmentDetails?id=" + id;
	    	}
		}
});