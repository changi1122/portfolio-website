
function toggleSectionOpen(e) {
    const section = e.currentTarget.parentElement;
    if (section.classList.contains("closed")) {
        section.classList.remove("closed");
    } else {
        section.classList.add("closed");
    }
}

document.querySelectorAll(".resume .title").forEach(function (element) {
    element.addEventListener("click", toggleSectionOpen);
})