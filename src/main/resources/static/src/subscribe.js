var url = "http://54.235.62.106:8080";

function subscribe() {
	let email = escape(document.getElementById('email').value);
	$.ajax({
		   url : url + "/subscribers?email=" + email,
		   type : 'POST',
		   processData: false,
		   contentType: false,
		   success : function(data) {
			   loadAllSubscibers();
		   }
	});
}

function unsubscribe(id) {
	$.ajax({
		   url : url + "/subscribers/" + id,
		   type : 'DELETE',
		   processData: false,
		   contentType: false,
		   success : function(data) {
			   loadAllSubscibers();
		   }
    });
}

function generateSubscriberHtml(subscriber, index) {
  return `
	<li class="list-group-item checkbox">
	  <div class="row">
		<div class="col-md-10 col-xs-10 col-lg-10 col-sm-10 task-text">
		  ${subscriber.email}
		</div>
		<div class="col-md-1 col-xs-1 col-lg-1 col-sm-1 delete-icon-area">
		  <a class="" href="#" onclick="unsubscribe(${subscriber.id});return false;"><i id="openImage" data-id="${index}" class="view-icon glyphicon glyphicon-trash"></i></a>
		</div>
	  </div>
	</li>
  `;
}

function loadAllSubscibers() {
	$.ajax({
		   url : url + "/subscribers",
		   type : 'GET',
		   processData: false,
		   contentType: false,
		   success : function(data) {
			   buildSubscribersTable(data);
		   }
	});
}

function buildSubscribersTable(data) {
  let subscribersHtml = data.reduce((html, subscriber, index) => html += generateSubscriberHtml(subscriber, index), '');
  document.getElementById('subscriberList').innerHTML = subscribersHtml;
}

window.addEventListener("load", () => {
    loadAllSubscibers()
});
