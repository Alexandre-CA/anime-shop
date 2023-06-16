const wraper = document.querySelector('.wraper');
const loginLink = document.querySelector('.login-link');
const registerLink = document.querySelector('.register-link');
const btnPopup = document.querySelector('.btnLogin-popup');
const iconClose = document.querySelector('.icon-close');
registerLink.addEventListener('click', () => {
    console.log(wraper);
    wraper.classList.add('active');
});
loginLink.addEventListener('click', () => {
    wraper.classList.remove('active');
});
// btnPopup.addEventListener('click',()=>{
//     wraper.classList.add('active-popup');
// });
// iconClose.addEventListener('click',()=>{
//     wraper.classList.remove('active-popup');
// });

setTimeout(() => {
    wraper.classList.add('active-popup');
}, 100)
const xhr = new XMLHttpRequest();
// xhr.open('GET','http://viacep.com.br/ws/74675530/json/',true);
// xhr.onload=()=>{console.log(xhr.responseText);};
// xhr.send(); 
function onLogin() {
    //Login
    const email = document.getElementById('emailLogin');
    const password = document.getElementById('passwordLogin');
    if (!email.value || !password.value) {
        alert('Preencha os campos');
    } else {
        //verificaçao de email e senha com o backend
        const payload = {
            email: email.value,
            password: password.value
        }
        xhr.open('POST', 'http://localhost:8080/auth/customer/login', true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    sessionStorage.setItem('userData', JSON.stringify(response.data));
                    setTimeout(() => {
                        window.location.href = 'home.html';
                    },1000)
                } else {
                    alert(response.msg);
                }
            }
        }
        xhr.send(JSON.stringify(payload));
    }
}
function onRegister() {
    //Registra
    const cep = document.getElementById('CEP-res');
    const nome = document.getElementById('nome-res');
    const email = document.getElementById('email-res');
    const password = document.getElementById('password-res');
    console.log(email.value);
    console.log(password.value);
    if (!email.value || !password.value || !nome.value || !cep.value) {
        alert('Preencha os campos');
    } else {
        //criação de usuario com o backend
        const payload = {
            name: nome.value,
            cep: cep.value,
            email: email.value,
            password: password.value

        }
        console.log(payload)
        xhr.open('POST', 'http://localhost:8080/customer', true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    setTimeout(() => {
                        window.location.href = 'index.html';
                    },100)
                } else {
                    alert(response.msg);
                }
            }
        }
        xhr.send(JSON.stringify(payload));
    }
}