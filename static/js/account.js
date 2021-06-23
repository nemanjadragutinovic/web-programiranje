function initialIsCapital( word ){
  return word.charAt(0) !== word.charAt(0).toLowerCase();
}


Vue.component("account", {
	data: function () {
		    return {
		    	user:null,
		    	username: '',
			    name: '',
			    surname: '',
			    gender: '',
			    savedPassword:'',
		    	oldpassword:'',
			    password:'',
			    passwordC:'',
			    againpassword:'',
			    usernameError:'',
			    nameError:'',
			    surnameError:'',
			    genderError:'',
			    againpasswordError:'',
			    passwordError:'',
			    uniqueError:'',
			    oldpasswordError:'',
			    backup:[],
			    mode:"BROWSE"
		    }
	},
	template: ` 
<div>
<table class="tabelaInformacijaOKorisniku">
<tr>
<td>
	<table v-bind:disabled="mode=='PASSWORD'" >
		
		<tr>
			<td>Ime:</td>
			<td><input class="input" v-bind:disabled="mode=='BROWSE' || mode=='PASSWORD'" placeholder="Unesite ime" type="text" v-model="name" name="name"/></td>
			<td ><p style="color: red" >{{nameError}}</p></td>	
		</tr>
		<tr>
			<td>Prezime:</td>
			<td><input class="input" v-bind:disabled="mode=='BROWSE' || mode=='PASSWORD'" placeholder="Unesite prezime" type="text" v-model="surname" name="surname"/></td>
			<td ><p style="color: red" >{{surnameError}}</p></td>	
		</tr>
		<tr>
			<td>Pol:</td>
			<td>
  				<div class="pol"><input type="radio" v-bind:disabled="mode=='BROWSE' || mode=='PASSWORD'" name="gender" v-model="gender" value="male"> Muski<br></div>
  				<div class="pol"><input type="radio" v-bind:disabled="mode=='BROWSE' || mode=='PASSWORD'" name="gender" v-model="gender" value="female"> Zenski<br></div>
			</td>
			<td ><p style="color: red" >{{genderError}}</p></td>	
		</tr>
		<tr>
			<td>Korisnicko ime:</td>
			<td><input class="input" disabled="true" type="text" v-model="username" name="username"/></td>
		</tr>
		<tr>
			<td align="left"><button v-on:click="checkFormValid" v-bind:hidden="mode!='EDIT'" class="buttonBlue">Sacuvaj</button><button v-on:click="izmeniKlik" v-bind:disabled="mode=='PASSWORD'" class="buttonRed">Izmeni</button></td>
			<td><button v-on:click="odustanakEvent" class="buttonRed">Odustanak</button><br /></td>
			
		</tr>
		<tr>
		<td><button v-on:click="paswordChange" class="buttonBlue">Izmena lozinke</button><br /></td>
		</tr>
	</table>
</td>
<tr>
<td>
	<table v-bind:hidden="mode!='PASSWORD'">
		<tr>
			<td>Stara lozinka:</td>
			<td><input class="input" placeholder="Unesite lozinku" v-model="oldpassword" type="password" name="oldpassword"/></td>
			<td ><p style="color: red" >{{oldpasswordError}}</p></td>		
		</tr>
		<tr>
			<td>Nova lozinka:</td>
			<td><input class="input" placeholder="Unesite lozinku" v-model="password" type="password" name="password"/></td>
			<td ><p style="color: red" >{{passwordError}}</p></td>		
		</tr>
		<tr>
			<td>Ponovo unesite lozinku:</td>
			<td><input class="input" placeholder="Ponovo unesite lozinku" v-model="againpassword" type="password" name="aggainpassword"/></td>	
			<td ><p style="color: red" >{{againpasswordError}}</p></td>		
		</tr>

		<tr>	
			<td colspan="3" align="center"><p style="color: red" >{{uniqueError}}</p></td>		
		</tr>
		<tr>
			<td  align="left"><button v-on:click="izmenaLozinke" class="buttonChangeUser">Izmeni lozinku</button></td><td><button v-on:click="odustanaPasswordkEvent" class="buttonOdustanak">Odustani</button><br /></td>
		</tr>
	</table>
</td>
</tr>
</tr>


</table>



</div>
`
	, 
	mounted(){
		axios
		.get("/users/log/test")
		.then(response => {
			if(response.data != null){
				this.user = response.data;
				this.username= response.data.username;
				this.name= response.data.name;
				this.surname= response.data.surname;
				this.gender =response.data.gender;
				this.savedPassword = response.data.password;
				this.passwordC = response.data.password;
				this.backup = [this.username, this.name, this.surname, this.gender];
			}else{
      		  window.location.href = "#/login";
			}
		})
	},
	methods : {
		checkFormValid : function() {
			this.usernameError = '';
			this.nameError='';
			this.surnameError='';
			this.genderError='';
			
			
			if(this.name == ""){
				this.nameError = "Ime je obavezno polje!"
			}
			else if(!initialIsCapital(this.name))
				this.nameError = 'Ime mora poceti velikim slovom!';
			else if(this.surname == "")
				this.surnameError = 'Prezime je obavezno polje!';
			else if(!initialIsCapital(this.surname))
				this.surnameError = 'Prezime mora poceti velikim slovom!';
			else if(this.gender == "")
				this.genderError =  'Pol je obavezno polje!';
			else
				{
		      			  this.mode = "BROWSE";
		      			  
		      			if(this.user.userType == 'Guest')
							user = {userType:this.user.userType ,username: this.user.username, name : this.name, surname : this.surname, gender : this.gender, password : this.savedPassword,rentedAppartments : this.user.rentedAppartments,
									reservations : this.user.reservations};
						else if(this.user.userType == 'Host')
							user = {userType:this.user.userType ,username: this.user.username, name : this.name, surname : this.surname, gender : this.gender, password : this.savedPassword,appartments : this.user.appartments};
						else
							user = {userType:this.user.userType ,username: this.user.username, name : this.name, surname : this.surname, gender : this.gender, password : this.savedPassword};
		      			  
		        		 axios
			  				.put("/users/update", JSON.stringify(user))
			  				.then(response => toast('Informacije su uspesno izmenjene'));
						 
		        		  this.user.username = this.username;
		        		  this.user.name = this.name;
		        		  this.user.surname = this.surname;
		        		  this.user.gender = this.gender;
		        		  
						}
		         
				
				
				
		},
		izmeniKlik : function(){
			this.mode = "EDIT";
		},
		odustanakEvent : function(){
			this.username= this.backup[0];
			this.name= this.backup[1];
			this.surname= this.backup[2];
			this.gender =this.backup[3];
			window.location.href = "#/";
		},
		paswordChange : function(){
			this.mode = "PASSWORD";
		},
		izmenaLozinke : function(){
			this.againpasswordError='';
			this.passwordError='';
			this.uniqueError='';
			this.oldpasswordError = '';
			
			if(this.oldpassword == "")
				this.oldpasswordError = "Stara lozinka se mora uneti!";
			else if(this.password == "")
				this.passwordError = 'Sifra je obavezno polje!';
			else if(this.againpassword == "")
				this.againpasswordError = 'Sifra se mora ponovo uneti!';
			else if(this.againpassword != this.password)
				this.passwordError =  'Sifre se moraju poklapati!';
			else if(this.savedPassword != this.oldpassword){
				this.oldpasswordError =  'Stara lozinka nije validna!';
			}else
				{	
				this.mode = "BROWSE";
				let user

				if(this.user.userType == 'Guest')
					user = {userType:this.user.userType ,username: this.user.username, name : this.user.name, surname : this.user.surname, gender : this.user.gender, password : this.password,rentedAppartments : this.user.rentedAppartments,
							reservations : this.user.reservations};
				else if(this.user.userType == 'Host')
					user = {userType:this.user.userType ,username: this.user.username, name : this.user.name, surname : this.user.surname, gender : this.user.gender, password : this.password,appartments : this.user.appartments};
				else
					user = {userType:this.user.userType ,username: this.user.username, name : this.user.name, surname : this.user.surname, gender : this.user.gender, password : this.password};
				
				axios
				.put("/users/update", JSON.stringify(user))
				.then(response => toast('Lozinka je uspesno izmenjena!'));
				
			    this.savedPassword= this.password;
			    this.oldpassword = this.password;
			    this.againpassword= '';
			}
		},
		odustanaPasswordkEvent: function(){
			this.mode = "BROWSE";
		}
	}
});

