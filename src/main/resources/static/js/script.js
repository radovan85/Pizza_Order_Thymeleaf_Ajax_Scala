window.onload = redirectHome;

function redirectHome() {
	$("#ajaxLoadedContent").load("/home");
}

function redirectLogin() {
	$("#ajaxLoadedContent").load("/login");
}

function redirectAllPizzasAdmin() {
	$("#ajaxLoadedContent").load("/admin/allPizzas");
}

function redirectAddPizza() {
	$("#ajaxLoadedContent").load("/admin/createPizza");
}

function redirectInvalidPath() {
	$("#ajaxLoadedContent").load("/admin/invalidPath");
}

function redirectPizzaDetails(pizzaId) {
	$("#ajaxLoadedContent").load("/admin/pizzaDetails/" + pizzaId);
}

function redirectUpdatePizza(pizzaId) {
	$("#ajaxLoadedContent").load("/admin/updatePizza/" + pizzaId);
}

function redirectAllSizesByPizza(pizzaId) {
	$("#ajaxLoadedContent").load("/admin/allSizes/" + pizzaId);
}

function redirectAllSizes() {
	$("#ajaxLoadedContent").load("/admin/allSizes");
}

function redirectAddPizzaSize() {
	$("#ajaxLoadedContent").load("/admin/createPizzaSize");
}

function redirectUpdatePizzaSize(pizzaSizeId) {
	$("#ajaxLoadedContent").load("/admin/updatePizzaSize/" + pizzaSizeId);
}

function redirectPizzaSizeDetails(pizzaSizeId) {
	$("#ajaxLoadedContent").load("/admin/pizzaSizeDetails/" + pizzaSizeId);
}

function redirectRegister() {
	$("#ajaxLoadedContent").load("/register");
}

function redirectAllPizzasUser() {
	$("#ajaxLoadedContent").load("/pizzas/allPizzas");
}

function redirectPizzaDetailsUser(pizzaId) {
	$("#ajaxLoadedContent").load("/pizzas/pizzaDetails/" + pizzaId);
}

function redirectAddPizzaToCart(pizzaId) {
	$("#ajaxLoadedContent").load("/carts/addToCart/" + pizzaId);
}

function redirectCart() {
	$("#ajaxLoadedContent").load("/carts/cart");
}

function redirectAddressConfirm() {
	$("#ajaxLoadedContent").load("/orders/confirmUserData");
}

function redirectCartError() {
	$("#ajaxLoadedContent").load("/carts/cartError");
}

function redirectPhoneConfirmation() {
	$("#ajaxLoadedContent").load("/orders/phoneConfirmation");
}

function redirectOrderConfirmation() {
	$("#ajaxLoadedContent").load("/orders/orderConfirmation");
}

function redirectCancelOrder() {
	$("#ajaxLoadedContent").load("/orders/cancelOrder");
}

function redirectOrderCompleted() {
	$("#ajaxLoadedContent").load("/orders/orderCompleted");
}

function redirectAllCustomers() {
	$("#ajaxLoadedContent").load("/admin/allCustomers");
}

function redirectCustomerDetails(customerId) {
	$("#ajaxLoadedContent").load("/admin/customerDetails/" + customerId);
}

function redirectAllOrders() {
	$("#ajaxLoadedContent").load("/admin/allOrders");
}

function redirectOrderDetails(orderId) {
	$("#ajaxLoadedContent").load("/admin/orderDetails/" + orderId);
}

function loginInterceptor(formName) {
	var $form = $("#" + formName);

	$form.on('submit', function(e) {
		e.preventDefault();

		$.ajax({
			url : "http://localhost:8080/login",
			type : 'post',
			data : $form.serialize(),
			success : function(response) {
				confirmLoginPass();
			},
			error : function(error) {
				alert("Failed");

			}
		});

	});
};

function confirmLoginPass() {
	$.ajax({
		url : "http://localhost:8080/loginPassConfirm",
		type : "POST",
		success : function(response) {
			checkForSuspension();
		},
		error : function(error) {
			$("#ajaxLoadedContent").load("/loginErrorPage");
		}
	});
};

function checkForSuspension() {
	$.ajax({
		url : "http://localhost:8080/suspensionChecker",
		type : "POST",
		success : function(response) {
			window.location.href = "/";
		},
		error : function(error) {
			$("#ajaxLoadedContent").load("/suspensionPage");
		}
	});
};

function redirectLogout() {
	$.ajax({
		type : "POST",
		url : "http://localhost:8080/loggedout",
		beforeSend : function(xhr) {
			xhr.overrideMimeType("text/plain; charset=x-user-defined");
		},
		success : function(data) {
			window.location.href = "/";
		},
		error : function(error) {
			alert("Logout error");

		}
	});
};

function formInterceptor(formName) {
	var $form = $("#" + formName);

	$form.on('submit', function(e) {
		e.preventDefault();
		if (validateRegForm()) {
			$.ajax({
				url : "http://localhost:8080/register",
				type : 'post',
				data : $form.serialize(),
				success : function(response) {
					$("#ajaxLoadedContent").load("/registerComplete");
				},
				error : function(error) {
					$("#ajaxLoadedContent").load("/registerFail");

				}
			});
		}
		;
	});
};

function executePizzaForm() {

	var formData = new FormData();
	var files = $('input[type=file]');
	for (var i = 0; i < files.length; i++) {
		if (files[i].value == "" || files[i].value == null) {
			alert("Please provide image");
			return false;
		} else {
			formData.append(files[i].name, files[i].files[0]);
		}
	}
	var formSerializeArray = $("#pizzaForm").serializeArray();
	for (var i = 0; i < formSerializeArray.length; i++) {
		formData
				.append(formSerializeArray[i].name, formSerializeArray[i].value)
	}
	if (validatePizza()) {
		$.ajax({
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			url : "http://localhost:8080/admin/createPizza",
			success : function(response) {
				$("#ajaxLoadedContent").load("admin/allPizzas");
			},
			error : function(error) {
				redirectInvalidPath();
			}
		});
	}
	;
};

function deletePizza(pizzaId) {
	if (confirm('Are you sure you want to remove this pizza?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deletePizza/" + pizzaId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectAllPizzasAdmin();
			},
			error : function(error) {
				alert("Failed");
			}
		});
	}
	;
};

function pizzaSizeInterceptor(formName) {
	var $form = $("#" + formName);

	$form.on('submit', function(e) {
		e.preventDefault();
		if (validatePizzaSize()) {
			$.ajax({
				url : "http://localhost:8080/admin/createPizzaSize",
				type : "POST",
				data : $form.serialize(),
				success : function(response) {
					redirectAllSizes();
				},
				error : function(error) {
					$("#ajaxLoadedContent").load("/admin/existingSizeError");

				}
			});
		}
		;
	});
};

function cartItemInterceptor(formName) {
	var $form = $("#" + formName);

	$form.on('submit', function(e) {
		e.preventDefault();
		if (validateCartItem()) {
			$.ajax({
				url : "http://localhost:8080/carts/storeCartItem",
				type : "POST",
				data : $form.serialize(),
				success : function(response) {
					$("#ajaxLoadedContent").load("/carts/itemAdded");
				},
				error : function(error) {
					$("#ajaxLoadedContent").load("/carts/itemsError");

				}
			});
		}
		;
	});
};

function deletePizzaSize(pizzaSizeId) {
	if (confirm('Are you sure you want to remove this pizza size?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deletePizzaSize/" + pizzaSizeId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectAllSizes();
			},
			error : function(error) {
				alert("Failed");
			}
		});
	}
	;
};

function eraseItem(cartId, itemId) {
	if (confirm('Remove this item from cart?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/carts/deleteItem/" + cartId + "/"
					+ itemId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectCart();
			},
			error : function(error) {
				alert("Failed");
			}
		});
	}
	;
};

function eraseAllItems(cartId) {
	if (confirm('Are you sure you want to clear your cart?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/carts/deleteAllItems/" + cartId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectCart();
			},
			error : function(error) {
				alert("Failed");
			}
		});
	}
	;
};

function redirectValidateCart(cartId) {
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/carts/validateCart/" + cartId,
		beforeSend : function(xhr) {
			xhr.overrideMimeType("text/plain; charset=x-user-defined");
		},
		success : function(data) {
			redirectAddressConfirm();
		},
		error : function(error) {
			redirectCartError();

		}
	});
};

function shippingAddressInterceptor(formName) {
	var $form = $("#" + formName);

	$form.on('submit', function(e) {
		e.preventDefault();
		if (validateShippingAddress()) {
			$.ajax({
				url : "http://localhost:8080/orders/confirmShippingAddress",
				type : "POST",
				data : $form.serialize(),
				success : function(response) {
					redirectPhoneConfirmation();
				},
				error : function(error) {
					alert("Failed");

				}
			});
		}
		;
	});
};

function customerInterceptor(formName) {
	var $form = $("#" + formName);

	$form.on('submit', function(e) {
		e.preventDefault();
		if (validateCustomer()) {
			$.ajax({
				url : "http://localhost:8080/orders/phoneConfirmation",
				type : "POST",
				data : $form.serialize(),
				success : function(response) {
					redirectOrderConfirmation();
				},
				error : function(error) {
					alert("Failed");

				}
			});
		}
		;
	});
};

function executeOrder() {
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/orders/createOrder",
		beforeSend : function(xhr) {
			xhr.overrideMimeType("text/plain; charset=x-user-defined");
		},
		success : function(data) {
			redirectOrderCompleted();
		},
		error : function(error) {
			alert("Failed");

		}
	});
};

function suspendUser(userId) {
	if (confirm('Are you sure you want to suspend this user?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/suspendUser/" + userId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectAllCustomers();
			},
			error : function(error) {
				alert("Failed");
			}
		});
	}
	;
};

function reactivateUser(userId) {
	if (confirm('Are you sure you want to reactivate this user?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/clearSuspension/" + userId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectAllCustomers();
			},
			error : function(error) {
				alert("Failed");
			}
		});
	}
	;
};

function deleteOrder(orderId) {
	if (confirm('Are you sure you want to clear this order?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteOrder/" + orderId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectAllOrders();
			},
			error : function(error) {
				console.log(error);
			}
		});
	}
	;
};

function deleteCustomer(customerId) {
	if (confirm('Are you sure you want to remove this customer?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteCustomer/" + customerId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectAllCustomers();
			},
			error : function(error) {
				alert("Failed");
			}
		});
	}
	;
};

function validateKeyword() {
	var returnValue = true;
	var keyword = $("#keyword").val();

	if (keyword === "") {
		returnValue = false;
	}

	return returnValue;
}

$("#searchForm").submit(function(event) {
	event.preventDefault();
});

$("#searchButton").on(
		"click",
		function() {
			var keyword = $("#keyword").val();
			if (validateKeyword()) {

				$("#ajaxLoadedContent").load(
						"/pizzas/searchPizza" + "?keyword=" + keyword);
			}
			;
		});
