console.log("Welcome to Gaurav Jangid Portfolio!");
let i=0,text = "Hello , I'm Gaurav Jangid";
function type()
{
document.getElementByID("typing").textContent = text.slice(0,i++);
if (i=>text.length) setTimeout(type, 100);
}
type();
