const registration = { template: '<registration></registration>' }
const HomePage = { template: '<home-page></home-page>' }
const account = { template: '<account></account>' }
const users = { template: '<users></users>' }
const amenities = { template: '<amenities></amenities>' }
const login = { template: '<login></login>' }
const apartment = { template: '<apartment></apartment>' }
const reservations = { template: '<reservations></reservations>' }
const apartmentDetails = { template: '<apartment-details></apartment-details>' }
const reservation = { template: '<reservation></reservation>' }

const router = new VueRouter({
	  mode: 'hash',
	  routes: [
	    { path: '/', component: HomePage},
	    { path: '/register', component: registration },	  
	    { path: '/account', component: account },
	    { path: '/users', component: users },
	    { path: '/amenities', component: amenities },
	    { path: '/login', component: login },
	    { path: '/apartment', component: apartment },
	    { path: '/reservations', component: reservations},
	    { path: '/apartmentDetails', component: apartmentDetails },
	    { path: '/reservation', component: reservation},
	   
	  ]
});

var app = new Vue({
	router,
	el: '#initialSearch',
	data: {
        mod: "PROBA"
	},
	mounted (){
		axios
        .get('/users/log/test')
        .then(response => {
        	if(response.data == null)
        		this.mod='USER';
        	else{
        		if(response.data.userType == "Guest")
        			this.mod='GUEST';
        		else if(response.data.userType == "Host")
        			this.mod='HOST';
        		else 
        			this.mod = 'ADMIN';
        	}
        })
	},
	methods : {
		odjavaEvents : function(){
			axios
	          .get('/users/log/logout')
	          .then(response => {
	        		this.mod='USER';

	        });
			
		}
	},components : { vuejsDatepicker }
});



