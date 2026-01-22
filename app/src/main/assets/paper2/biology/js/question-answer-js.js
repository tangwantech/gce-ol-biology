
const allAnswerDivs = document.querySelectorAll(".answer")
const backButton = document.querySelector("#btn-back")


for (let index = 0; index < allAnswerDivs.length; index++) {
    const element = allAnswerDivs[index];
    const btn = element.querySelector(".btn-view-answer")
    const divAnswer = element.querySelector(".div-answer")
    
    divAnswer.setAttribute("data-answer", 0)

    btn.addEventListener("click", () =>{
        
        const attr = divAnswer.getAttribute("data-answer")
        console.log(attr)

        if(attr === "0"){
            divAnswer.setAttribute("data-answer", 1)
            divAnswer.style.display = "block"
            btn.innerHTML = "Hide answer"  
        }else{
            divAnswer.setAttribute("data-answer", 0)
            divAnswer.style.display = "none"
            btn.innerHTML = "Answer"
        }
        
        
    })
    
}



