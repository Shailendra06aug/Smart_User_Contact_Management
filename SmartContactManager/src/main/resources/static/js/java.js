console.log("this is script file")

const toggleSidebars=()=>{

if($(".sidebar").is(":visible")){
//true
// Band karana hai
 $(".sidebar").css("display", "none")
 $(".content").css("margin-left", "1%")
}
else{
//false
// show karna hai
  $(".sidebar").css("display", "block")
  $(".content").css("margin-left", "20%")
}

};


const search=()=>{
//console.log("searching data....")

let query= $("#search-input").val();


if(query==''){
  $(".search-result").hide();

}
else{
//search
console.log(query)
//Sending Request to backend

let url= `http://localhost:8080/search/${query}`;

fetch(url).then((response)=> {
  return response.json();
}).then((data)=>{
//data...
// console.log(data);

let text= `<div class='list-group'>`

data.forEach(contact=>{
text+=`<a href='/user/${contact.cId}/contact/' class='list-group-item list-group-item-action'>
 ${contact.name}</a>`
});

text+=`</div>`;

$(".search-result").html(text);
$(".search-result").show();

});


$(".search-result").show();


}


}



