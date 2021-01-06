/*
GAME RULES
- The game has 2 players,playing in rounds
- In Each turn,a player rolls a dice as many times as he wishes.Each results get added to his ROUND score
- BUT, if the player rolls a 1,all his ROUND score gets lost.After that ,it's the next player's turn
- The Player can choose to HOLD ,which means that his ROUND score gets added to his GLOBAL score. After that,it's the next player's turn
- the first player to reach 100 points on GLOBAL score wins the game 
*/
var scores,turn,local,player1,player2;
var names = ["player_one","player_two"];
var max = 25;
scores = [0,0];
turn = 0;
local = 0;

console.log(document.getElementsByClassName("button")[0].disabled);
document.getElementById("b1").disabled = true;
document.getElementById("b2").disabled = true;
console.log(document.getElementsByClassName("button")[0].disabled);
document.getElementById("playerName1").value = "";
document.getElementById("playerName2").value = "";

play = document.getElementsByClassName("play")[0];
box = document.getElementsByClassName("box")[0];

var roll = document.getElementsByClassName("reload")[0];
var dice = document.getElementsByClassName("diceimg")[0];
var hold = document.getElementsByClassName("hold")[0];

hold.onclick = function()
{
    console.log(turn);
    document.getElementById("pimg").animate([{transform: 'rotate(0)'},{transform: 'rotate(360deg)'}],{duration:400,iterations:1});
    scores[turn] += local;
    document.getElementsByClassName("global")[turn].innerHTML = scores[turn];
    local = 0 ;
    document.getElementsByClassName("sc")[turn].innerHTML = local;
    if(scores[turn]>= max)
    {            
        document.getElementById(names[turn]).innerHTML = "WINNER!";
        document.getElementById(names[turn]).style.backgroundColor = "#f7022a";
    }
    else
    {    
        //document.getElementById(names[turn]).style.backgroundColor = "transparent";
        document.getElementById(names[turn]).animate([{backgroundColor : "#f7022a",color : "white"},{backgroundColor : "transparent",color : "black"}],{fill:"forwards",duration:500,iterations:1});
        turn = (turn+1)%2;
        //document.getElementById(names[turn]).style.backgroundColor = "#f7022a";
        document.getElementById(names[turn]).animate([{backgroundColor : "transparent",color : "black"},{backgroundColor : "#f7022a",color : "white"}],{fill:"forwards",duration:400,iterations:1});
    }
    /*
    document.getElementById("pimg").animate([{transform: 'rotate(0)'},{transform: 'rotate(360deg)'}],{duration:400,iterations:1});
    if(turn == 0)
    {
        scores[0] += local;
        document.getElementsByClassName("global")[0].innerHTML = scores[0];
        local = 0 ;
        document.getElementsByClassName("sc")[0].innerHTML = local;
        if(scores[0]>= max)
            document.getElementById("player_one").innerHTML = "WINNNER!";
    }
    else
    {
        scores[1] += local;
        document.getElementsByClassName("global")[1].innerHTML = scores[1];
        local = 0 ;
        document.getElementsByClassName("sc")[1].innerHTML = local;
        
    }
    if(scores[turn]>= max)
    {
        document.getElementById("player_two").innerHTML = "WINNNER!";
    }
    else
    {
        document.getElementById(names[turn]).style.backgroundColor = "transparent";
        document.getElementById(names[turn]).style.color = "black";
    
        turn = (turn + 1) % 2;
        document.getElementById(names[turn]).style.backgroundColor = "#f7022a";
        document.getElementById(names[turn]).style.color = "white";    
    }*/
}

roll.addEventListener('click',function()
{
    dice.animate([{transform: 'rotate(0)'},{transform: 'rotate(360deg)'}],{fill:"forwards",duration:400,iterations:1});
});

roll.onclick = function()
{
    document.getElementById("rimg").animate([{transform: 'rotate(0)'},{transform: 'rotate(360deg)'}],{fill:"forwards",duration:400,iterations:1});
    var number  = Math.floor((Math.random()*6) + 1)
    dice.src = "src/red/"+number+"\.svg"
    if(number == 1)
    {
        local = 0;
        document.getElementsByClassName("sc")[turn].innerHTML = local;

        //document.getElementById(names[turn]).style.backgroundColor = "transparent";
        document.getElementById(names[turn]).animate([{backgroundColor : "#f7022a",color : "white"},{backgroundColor : "transparent",color : "black"}],{fill:"forwards",duration:500,iterations:1});
        turn = (turn+1)%2;
        //document.getElementById(names[turn]).style.backgroundColor = "#f7022a";
        document.getElementById(names[turn]).animate([{backgroundColor : "transparent",color : "black"},{backgroundColor : "#f7022a",color : "white"}],{fill:"forwards",duration:400,iterations:1});

                /*
        document.getElementById(names[turn]).style.backgroundColor = "transparent";
        document.getElementById(names[turn]).style.color = "black";
    
        turn = (turn + 1) % 2;
        document.getElementById(names[turn]).style.backgroundColor = "#f7022a";
        document.getElementById(names[turn]).style.color = "white";*/
        }
    else
    {
        console.log(turn);
        local += number;
        if(turn == 0)
        {
            document.getElementsByClassName("sc")[0].innerHTML = local;  
        }    
        else
        {
            document.getElementsByClassName("sc")[1].innerHTML = local;  
        }    
    }
}

play.onclick = function()
{

    document.getElementById("playButton").animate([{transform: 'rotate(0)'},{transform: 'rotate(180deg)'}],{fill:"forwards",duration:400,iterations:1});
    player1 = document.getElementById("playerName1").value.trim();
    player2 = document.getElementById("playerName2").value.trim();
    
    if (!player1 || !player2)
    {
        document.querySelector(".error").style.display = "block";
        document.getElementsByClassName("error")[0].animate([{visibility: 'hidden'},{visibility: 'visible'}],{fill:"forwards",duration:400,iterations:1});
        document.getElementById("errorButton").animate([{transform: 'scale(0)',visibility: 'hidden'},{transform: 'scale(1)',visibility: 'visible'}],{fill:"forwards",duration:100,iterations:1});
    }
    else
    {
        document.getElementById("player_one").innerHTML = player1.toUpperCase();
        document.getElementById("player_two").innerHTML = player2.toUpperCase();
        console.log(document.getElementById("player_one").innerHTML);
        console.log(document.getElementById("player_two").innerHTML);
        box.style.display = "none";
        document.getElementById("b1").disabled = false;
        document.getElementById("b2").disabled = false;
        document.getElementById(names[turn]).animate([{backgroundColor : "transparent",color : "black"},{backgroundColor : "#f7022a",color : "white"}],{fill:"forwards",duration:400,iterations:1});
    }
}

document.querySelector("#errorButton").onclick = function()
{
    console.log(document.getElementsByClassName("error")[0]);
    document.getElementById("errorButton").animate([{transform: 'scale(1)',visibility: 'visible'},{transform: 'scale(0)',visibility: 'hidden'}],{fill:"forwards",duration:100,iterations:1});
    document.getElementsByClassName("error")[0].animate([{visibility: 'visible'},{visibility: 'hidden'}],{fill:"forwards",duration:400,iterations:1});
    document.getElementsByClassName("error")[0].style.visibility = 'visible';
    document.getElementById("errorButton").style.visibility = 'visible';
    console.log(document.getElementsByClassName("error")[0]);
    
}

document.querySelector("#closeButton").onclick = function()
{
    document.getElementsByClassName("box")[0].animate([{transform: 'scale(1)'},{transform: 'scale(0)',display : 'none'}],{fill:"forwards",duration:200,iterations:1});
    document.getElementById(names[turn]).animate([{backgroundColor : "transparent",color : "black"},{backgroundColor : "#f7022a",color : "white"}],{fill:"forwards",duration:400,iterations:1});
}

document.querySelector(".newgame_button").onclick = function()
{
    document.querySelector(".newgame_button").style = "border: transparent;";
    scores = [0,0];
    turn = 0;
    local = 0;
    document.getElementsByClassName("global")[0].innerHTML = scores[0];
    document.getElementsByClassName("global")[1].innerHTML = scores[1];
    document.getElementsByClassName("sc")[0].innerHTML = local;  
    document.getElementsByClassName("sc")[1].innerHTML = local;  
    document.getElementById("playerName1").value = "";
    document.getElementById("playerName2").value = "";
    document.querySelector(".box").style.display = "block";
    document.getElementsByClassName("box")[0].animate([{transform: 'scale(0)'},{transform: 'scale(1)',display : 'block'}],{fill:"forwards",duration:200,iterations:1});
}