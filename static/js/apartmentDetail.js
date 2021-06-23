Vue.component("apartment-details", {
	data: function () {
	    return {
	    	disabledDates : {},
	        apartment:{},
	        picture:'',
	        userType:'',       
	        adress: {},
	        comments:[],
	        user:null,
	        canReserve:null,	       
	        isActive:null,
	        dateFrom : '',
	        dateTo : '',
	        error:''
	    }
	},
	template: ` 
<div>
<table >
<tr>
		<td id="slike">
						
			<div class="containerPicture" ref="container">
				
				<img  :src="picture" style="width:70%"/>
			  	
			</div>
			
			<div class="rowS">
					<div v-for="p in apartment.pictures" class="columnS">
							<img :src="p" alt="Slika apartmana" style="width:80%" v-on:click="currentImage(p)"/>
					</div>
			</div>
		</td>
		
		
		<td id="info">
			<table id="apartmanInfo" style="background : rgb(255,255,255);">
				<tr>
					<td>Tip: </td>
					<td style=" color:rgb(152,0,0)">{{(apartment.type == 'apartment') ? "Apartman" : "Soba"}}</td>
				</tr>
				<tr>
					<td>Broj soba: </td>
					<td style=" color:rgb(152,0,0)">{{apartment.numberOfRoom}}</td>
				</tr>
				<tr>
					<td>Maksimalan broj gostiju: </td>
					<td style=" color:rgb(152,0,0)">{{apartment.numberOfGuest}}</td>
				</tr>
				
				<tr>
					<td>Cena za jednu noc: </td>
					<td style=" color:rgb(152,0,0)">{{apartment.priceForNight + ' dinara'}}</td>
				</tr>
				
				<tr>
					<td>Adresa: </td>
					<td >{{adress.street + ' ' + adress.streetNumber + ', ' + adress.postNumber + ' ' + adress.city}}</td>
				</tr>
				
				<tr>
					<td>Vreme ulaska u apartman: </td>
					<td >{{apartment.checkInTime}}</td>
				</tr>
				<tr>
					<td>Vreme izlaska iz apartmana: </td>
					<td >{{apartment.checkOutTime}}</td>
				</tr>
				
				<tr>
					<td>Sadrzaji apartmana: </td>
					<td>
					<table>
						<tr v-for="a in apartment.amenities">
							<td >{{a.name}}</td>
						</tr>
					</table>
					
					
					</td>
				</tr>
				
				
				<br>
				<tr v-bind:hidden="userType != 'HOST'"><td colspan="2">Dodavanje perioda za izdavanje: </td></tr>
				<tr v-bind:hidden="userType != 'HOST'">
					<td>Datum od: <vuejs-datepicker :disabled-dates="disabledDates" v-model="dateFrom"></vuejs-datepicker></td>
					<td>Datum do: <vuejs-datepicker :disabled-dates="disabledDates" v-model="dateTo"></vuejs-datepicker></td>
				</tr>
				<tr v-bind:hidden="userType != 'HOST'">
					<td colspan="2"><button class="buttonBlue" v-on:click="addNewPeriod">Dodaj</button><br/></td>
				</tr>
				<tr>
					<p style="color: red; font-size: 16px;" >{{error}}</p>
				</tr>	
									
								
								
				
				<tr v-bind:hidden="userType != 'GUEST'">
					<td colspan="2"><button class="buttonBlue" v-on:click="rezervisiClick">Rezervisi</button><br/></td>
				</tr>
				
					
				
				
			</table>
		</td>	
</tr>

<tr>

		<table>
		
							
																				
				<tr v-bind:hidden="userType != 'HOST' && userType != 'ADMIN'">
					<td v-bind:hidden="isActive === 'active'" ><button style="margin-left:130px;"  class="buttonBlue" v-on:click="activate">Aktiviraj apartman</button><br/></td>
					<td v-bind:hidden="isActive === 'inactive'" ><button  style="margin-left:130px;" class="buttonRed" v-on:click="deactivate">Deaktiviraj apartman</button><br/></td>
					<td><button style="margin-left:20px;" class="buttonRed" v-on:click="deleteApartment">Obrisi apartman</button><br/></td>
				</tr>
		
			
				
			
			
			
			
		</table>


</tr>




</table>



</div>		  
`, components : { 
		vuejsDatepicker
	},
	mounted () {
		axios
		.get('/apartment/' + this.$route.query.id)
		.then(response => {
			this.apartment = response.data; 
			this.adress = this.apartment.location.adress; 
			this.comments = this.apartment.comments; 
			this.picture = this.apartment.pictures[0]; 
			this.isActive= this.apartment.status
			let ranges = [];

			for(let d of this.apartment.dateForRenting){
				ranges.push({from : new Date(d.dateFrom), to : new Date(d.dateTo)});
			}
			
			this.disabledDates["ranges"] = ranges;
			this.disabledDates["to"] = new Date();

		});
		
				
		axios
        .get('/users/log/test')
        .then(response => {
        	if(response.data == null){
        		
        		this.userType = 'USER';
        	}
        	else{
        		this.user = response.data;
        		if(response.data.userType == "Guest"){
        			
            		this.userType = 'GUEST';
        		}
        		else if(response.data.userType == "Host"){
        			
            		this.userType = 'HOST';
        		}else{
        			
            		this.userType = 'ADMIN';
        		}
        	}
        })
	},
	methods : {
		currentImage : function(selectedPicture) {
				this.picture = selectedPicture;
				
		},
		
		rezervisiClick : function(){
			
			window.location.href = "#/reservation?id=" + this.$route.query.id;
			
		},
		
		activate:function(){
			this.apartment.status='active';
			this.isActive='active'
				axios
	    		.post("/apartment/updateCurrentApartment", this.apartment)
	    		.then(response => toast("Uspesno je izvrsena aktivacija! "));
		},
		
		deactivate:function(){
			this.apartment.status='inactive';
			this.isActive='inactive'
			
				axios
	    		.post("/apartment/updateCurrentApartment", this.apartment)
	    		.then(response => toast("Uspesno je izvrsena deaktivacija! "));
		},
		
		addNewPeriod: function(){
			this.error = '';
			if(this.dateFrom != '' && this.dateTo != ''){
				if(this.dateFrom.getTime() <= this.dateTo.getTime()){
					let datumOd = (new Date(this.dateFrom.getFullYear(),this.dateFrom.getMonth() , this.dateFrom.getDate())).getTime();
					let datumDo = (new Date(this.dateTo.getFullYear(),this.dateTo.getMonth() , this.dateTo.getDate())).getTime();
					let eror = false;
					let validniDatumi = [];
					
					for(let d of this.disabledDates.ranges){
						if((datumOd >= d.from.getTime() && datumDo<=d.to.getTime()) || (datumOd <= d.from.getTime() && datumDo>=d.to.getTime())
								||  (datumOd <= d.from.getTime() && (datumDo >= d.from.getTime() && datumDo<=d.to.getTime()))
								|| ((datumOd >= d.from.getTime() && datumOd<=d.to.getTime()) && datumDo >= d.from.getTime()))
						{
							
							eror = true;
							
						}
							
					}
						
						
						
					
					if(eror === true)
					{
						this.error = 'Period vec postoji! ';
					}else
					{
												
						while(datumOd <= datumDo){
							validniDatumi.push(datumOd);
							datumOd = datumOd + 24*60*60*1000;
							
						}
						
						
						this.apartment.dateForRenting.push({ dateFrom: datumOd , dateTo: datumDo });
						for(let a of  validniDatumi)
							this.apartment.freeDateForRenting.push(a);
						axios
			    		.post("/apartment/AddNewPeriod", this.apartment)
			    		.then(response => toast("Period je uspesno dodat!"));
					}
				}else{
					this.error = 'Datum od mora biti manji od datuma do!';
				}
			}else{
				
				this.error = 'Datum od i datum do se moraju uneti!';
				
			}
		},
		
		deleteApartment: function(){
			axios
    		.delete("/apartment/deleteSelectedApartment/" + this.$route.query.id)
    		.then(response => {
    			
    			toast("Apartman je uspesno obrisan! ")
    			window.location.href= "/";
    		});
		},
		
	}
});