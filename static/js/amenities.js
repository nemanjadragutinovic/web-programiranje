Vue.component("amenities", {
	data: function () {
		    return {
		        amenities: null,
		        nameError:'',
		        mode: "NOT_EDIT",
		        amenitiename:'',
		        selectedAmenity: {},
		        width:'60%'
		    }
	},
	template: ` 
	
<div id="sadrzaj" >


<form v-on:submit.prevent="addAmanity" method="post">
	<table>
		<tr>
			<td class="amenitytd">Naziv sadrzaja:</td>
			<td class="amenitytd"><input class="input" placeholder="Naziv sadrzaja" type="text" v-model="amenitiename" name="amenitiename"/></td>
			<td class="amenitytd"><p style="color: red" >{{nameError}}</p></td>	
			<td colspan="3" align="center"><input type="submit" id="submit" value="Dodaj"/></td>
		</tr>
		
	</table>
</form>



<h2>Dostupni sadrzaji za apartmane</h2>

<table class="glavna">
<tr>
	<td>
		<table class="sadrzaji" v-bind:style="{ width: computedWidth }">
			<tr>
				<th>ID</th>
				<th>Naziv sadrzaja</th>
			</tr>
			<tr v-for="amenity in amenities"  v-on:click="selectAmenity(amenity)"  v-bind:class="{selected : selectedAmenity.id===amenity.id}">
					<td>{{amenity.id }}</td>
					<td>{{amenity.name }}</td>
			</tr>
		</table>
	</td>
	<td>
			<table  v-bind:hidden="mode=='NOT_EDIT'">
				<tr>
					<td class="amenitytd">Naziv sadrzaja:</td>
				</tr>
				<tr>
					<td><input class="input" placeholder="Naziv sadrzaja" type="text"  v-model="selectedAmenity.name" v-bind:disabled="mode=='NOT_EDIT'" name="amenitiename"/></td>
				</tr>
				<tr>
					<td align="center"><input type="submit" id="submit" v-on:click="updateAmenity(selectedAmenity)" value="Sacuvaj"/>	<button v-on:click="cancelEditing" id="buttonOdustanak" v-bind:disabled="mode=='NOT_EDIT'">Odustanak</button> <br /></td>
				</tr>
			</table>
	</td>
</tr>

</table>

<div>
<table class="polozajAmenitiesDugmica">
<tr>
		<td><button v-on:click="editAmenity" v-bind:disabled="selectedAmenity==null" class="buttonBlue">Izmeni</button><br />
		<td><button v-on:click="deleteAmenity(selectedAmenity)" v-bind:disabled="selectedAmenity==null" class="buttonBlue">Izbrisi</button><br />

</tr>
</table>
</div>

</div>

`
	, 
	mounted () {
		axios
		.get("/users/log/test")
		.then(response => {
			if(response.data == null){
	      		  window.location.href = "#/login";
			}
		});
		
        axios
          .get('/amenities')
          .then(response => (this.amenities = response.data));
    },
    computed: {
        computedWidth: function () {
          return this.width;
        }
      },
      methods : {
			addAmanity : function() {
				this.nameError = '';
				
				if(this.amenitiename == "")
					this.nameError = 'Morate uneti sadrzaj!';
				else{
					
					let amenity = {name: this.amenitiename, id : 0};
	        		  axios
			          .post('/amenities/add', JSON.stringify(amenity))
			          .then(response => {
			        	  toast('Sadrzaj ' + this.amenitiename + ' uspesno dodat!');
			        	  
			        	  if(!this.amenities)
			        		  this.amenities = [response.data];
			        	  else{
			        		  this.amenities.push(response.data);
			        	  }
			          });
				}
			},
			updateAmenity: function(amenity){
				axios
	    		.post("/amenities", amenity)
	    		.then(response => toast('Sadrzaj ' + amenity.name + " uspešno izmenjen."));
	    		this.mode = 'NOT_EDIT';
	    		this.width = '60%';
			},
			editAmenity : function() {
	    		if (this.selectedAmenity.id == undefined)
	    			return;
	    		this.backup = [this.selectedAmenity.id, this.selectedAmenity.name];
	    		this.mode = 'EDIT';
	    		
	    		this.width = '100%';
	    	},
	    	selectAmenity : function(amenity) {
	    		if (this.mode == 'NOT_EDIT') {
	    			this.selectedAmenity = amenity;
	    		}
	    	},
	    	cancelEditing : function() {
	    		this.selectedAmenity.id = this.backup[0];
	    		this.selectedAmenity.name = this.backup[1];
	    		this.mode = 'NOT_EDIT';
	    		this.width = '60%';
	    	},
	    	
	    	deleteAmenity : function(amenity){
	    		let id = amenity.id;
	    		axios
	    		.delete("/amenities/" + id)
	    		.then(response => {
	    				
	    			for(let a of this.amenities){
	    				if(a.id === id){
	    					const index = this.amenities.indexOf(a);
	    					this.amenities.splice(index, 1);
	    					break;
	    				}
	    			}
	    			toast('Sadrzaj ' + amenity.name + " uspešno izbrisan.");
	    		});
	    	}
	}
	
});
