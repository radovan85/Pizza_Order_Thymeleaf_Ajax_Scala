function validatePizza() {

	var name = $("#name").val();
	var description = $("#description").val();

	var returnValue = true;

	if (name === "") {
		$("#nameError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#nameError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (description === "") {
		$("#descriptionError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#descriptionError").css({
			"visibility" : "hidden"
		});
	}
	;

	return returnValue;

};

function validatePizzaSize() {
	var pizzaId = $("#pizzaId").val();
	var name = $("#name").val();
	var price = $("#price").val();

	var priceNum = Number(price);
	var returnValue = true;

	if (name === "") {
		$("#nameError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#nameError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (pizzaId === "") {
		$("#pizzaIdError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#pizzaIdError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (price = "" || priceNum <= 0) {
		$("#priceError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#priceError").css({
			"visibility" : "hidden"
		});
	}

	return returnValue;
};

function validateRegForm() {
	var firstName = $("#firstName").val();
	var lastName = $("#lastName").val();
	var email = $("#email").val();
	var password = $("#password").val();
	var address = $("#address").val();
	var city = $("#city").val();
	var zipcode = $("#zipcode").val();
	var phone = $("#phone").val();

	var regEmail = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/g;
	var returnValue = true;

	if (firstName === "") {
		$("#firstNameError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#firstNameError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (lastName === "") {
		$("#lastNameError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#lastNameError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (email === "" || !regEmail.test(email)) {
		$("#emailError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#emailError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (password === "" || password.length < 6) {
		$("#passwordError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#passwordError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (address === "") {
		$("#addressError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#addressError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (city === "") {
		$("#cityError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#cityError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (zipcode === "") {
		$("#zipcodeError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#zipcodeError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (phone === "" || phone.length < 9 || phone.length > 15) {
		$("#phoneError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#phoneError").css({
			"visibility" : "hidden"
		});
	}
	;

	return returnValue;
}

function validateCartItem() {
	var quantity = $("#quantity").val();
	var pizzaSizeId = $("#pizzaSizeId").val();

	var quantityNum = Number(quantity);
	var returnValue = true;

	if (quantity === "" || quantityNum < 1 || quantityNum > 20) {
		$("#quantityError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#quantityError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (pizzaSizeId === "") {
		$("#pizzaSizeIdError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#pizzaSizeIdError").css({
			"visibility" : "hidden"
		});
	}
	;

	return returnValue;
};

function validateShippingAddress() {
	var city = document.getElementById("city").value;
	var zipcode = document.getElementById("zipcode").value;
	var address = document.getElementById("address").value;

	var returnValue = true;

	if (city === "") {
		$("#cityError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#cityError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (address === "") {
		$("#addressError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#addressError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (zipcode === "") {
		$("#zipcodeError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#zipcodeError").css({
			"visibility" : "hidden"
		});
	}
	;

	return returnValue;
};

function validateCustomer() {
	var customerPhone = $("#customerPhone").val();
	var returnValue = true;

	if (customerPhone === "" || customerPhone.length < 9 || customerPhone.length > 15) {
		$("#customerPhoneError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#customeerPhonError").css({
			"visibility" : "hidden"
		});
	}
	;

	return returnValue;

};

function validatePassword() {
	var password = document.getElementById("password").value;
	var confirmpass = document.getElementById("confirmpass").value;
	if (password != confirmpass) {
		alert("Password does Not Match.");
		return false;
	}
	return true;
};

function validateNumber(e) {
	var pattern = /^\d{0,4}(\.\d{0,4})?$/g;

	return pattern.test(e.key)
};