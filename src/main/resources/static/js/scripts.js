// var minutes = 60.1;
// function elapse() {
//     minutes -= 1 / 60;
//     document.getElementById('time').innerText = minuteConvert(minutes)
// }


// function minuteConvert(d) {
//     d = Number(d);
//     var h = Math.floor(d / 60);
//     var m = Math.floor(d % 60);
//     var s = Math.floor(((d % 60) % 1) * 60);

//     var hDisplay = h > 0 ? h + (h == 1 ? " hour  " : " hours  ") : "";
//     var mDisplay = m > 0 ? m + (m == 1 ? " minute  " : " minutes  ") : "";
//     var sDisplay = s > 0 ? s + (s == 1 ? " second" : " second") : "";
//     return hDisplay + mDisplay + sDisplay;
// }

// setInterval(elapse, 1000);


function Enroll(url) {
  swal({
    title: "Are you sure you want to enroll this course?",
    text: "Once you enroll, you can leave if you want.",
    buttons: true,
    dangerMode: true
  }).then(willAccept => {
    if (willAccept) {
      $.ajax(
        {
          type: "POST",
          url: url,
          success: function(data) {
            if (data) {
              window.location.href = "/";
            }
          },
          error: function() {
            alert('You have already enrolled this course');
          }
        }
      )
    }
  });
}

function Delete(url) {
    swal({
        title: "Are you sure you want to delete?",
        text: "You will not able to restore this data",
        icon: "warning",
        buttons: true,
        dangerMode: true
    }).then((willDelete) => {
        if (willDelete) {
            $.ajax({
                type: "DELETE",
                url: url,
                success: function(data) {
                    if (data) {
                        location.reload();
                        console.log("Delete operation is successful");
                    } else {
                        console.log("Error after delete");
                    }
                }
            })
        } else {
            console.log("No operation!");
        }
    });
}

function Download(url) {
  $.ajax({
    type: "GET",
    url: url,
    success: function (data) {
      alert("downloaded")
    },
    error: function() {
      alert("Can not download resource");
    }
  });
}