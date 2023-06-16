// verificar se o conteudo carregou
// if (document.readyState == "loading") {
//     document.addEventListener("DOMContentLoaded", ready)
// } else {
//     ready()
//     updateTotal()
// }

// function ready() {
//     const removeProductButtons = document.getElementsByClassName("remove-product-button")
//     for (var i = 0; i < removeProductButtons.length; i++) {
//         removeProductButtons[i].addEventListener("click", removeProduct)
//     }

//     const quantityInput = document.getElementsByClassName("product-qtd-input")
//     for (var i = 0; quantityInput.length; i++) {
//         quantityInput[i].addEventListener("change", updateTotal)
//     }
// }

// remover produto do carrinho
// function removeProduct(event) {
//     event.target.parentElement.parentElement.remove()
//     updateTotal()
// }

// fazer a soma do total dos preços
// function updateTotal() {
//     let totalAmount = 0
//     const cartProduct = document.getElementsByClassName("cart-product")
//     for (var i = 0; i < cartProduct.length; i++) {
//         const ProductPrice = cartProduct[i].getElementsByClassName("cart-product-price")[0].innerText.replace("R$", "").replace(",", ".")
//         const productQuantity = cartProduct[i].getElementsByClassName("product-qtd-input")[0].value
//         totalAmount += (ProductPrice * productQuantity)
//     }
//     totalAmount = totalAmount.toFixed(2)
//     totalAmount = totalAmount.replace(".", ",")
//     document.querySelector(".Sum-price").innerText = "R$" + totalAmount
// }

// adicionar produto ao carrinho
// function addProduct() {
//     let newCartProduct = document.createElement("tr")
//     newCartProduct.classList.add("cart-product")
//     newCartProduct.innerHTML =
//         `<td class="product-identification">
//     <img  class="cart-product-image" src="https://kbimages1-a.akamaihd.net/453665f3-f9c9-4775-834a-ec3179e86c80/353/569/90/False/prison-school-01.jpg" alt="">
//     <strong class="cart-product-title">Brand</strong>
// </td>
// <td>
//     <span class="cart-product-price">89,90R$</span>
// </td>
// `
//     const tableBody = document.querySelector(".cart-product tbody")
//     tableBody.append(newCartProduct)
// }
let products = [];
const getTotalPrice = () => {
    let total = 0;
    products.forEach((el) => {
        total += el.price * el.qtd;
    })
    const decimal = new Intl.NumberFormat('pt-BR', {
        style: 'decimal',
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
    document.querySelector(".Sum-price").innerHTML = `R$ ${decimal.format(total)}`;
}
const getProduct = () => {

    const sessionStorageProducts = JSON.parse(sessionStorage.getItem('product'));
    products = sessionStorageProducts;
    addHTMLProducts(products);
    getTotalPrice();
}
function removeProduct(index) {
    products.splice(index, 1);
    sessionStorage.setItem('product', JSON.stringify(products));
    addHTMLProducts(products);
    getTotalPrice();
}
const addHTMLProducts = (pds) => {
    const decimal = new Intl.NumberFormat('pt-BR', {
        style: 'decimal',
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
    let productsString = "";
    pds.forEach((el, idx) => {
        productsString += `
        <tr class="cart-product">
        <td class="product-identification">
            <img  class="cart-product-image" src="${el.image}" alt="">
            <strong class="cart-product-title">${el.name}</strong>
        </td>
        <td>
            <span class="cart-product-price">${decimal.format(el.price - (el.price * el.promotion))}</span>
        </td>
        <td>
            <input class="product-qtd-input" onChange="onChangeQtd(${idx})" type="number" value="${el.qtd}">
        </td>
        <td>
            <button class="remove-product-button" onClick="removeProduct(${idx})">Remover</button>
        </td>`
    })
    document.getElementById("productBody").innerHTML = productsString;

}
function onChangeQtd(index) {
    const inputs = document.getElementsByClassName('product-qtd-input')
    for (let i = 0; i < inputs.length; i++) {
        if (i === index) {
            products[i].qtd = inputs[i].value;
        }
    }
    getTotalPrice();
}

function finish() {
    const user = JSON.parse(sessionStorage.getItem('userData'));
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `http://viacep.com.br/ws/${user.cep}/json/`, true);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let response = JSON.parse(xhr.responseText);
            const payload = {
                name: user.name,
                address: `${response.logradouro ? response.logradouro + ',' : ''}${response.bairro ? response.bairro + ',' : ''}${response.localidade ? response.localidade : ''}`,
                state: response.uf,
                products:[]
            }
            products.forEach((el)=>{
                for(let i = 0; i < el.qtd; i++){
                    payload.products.push(el.id);
                }
            })
            const xhr2 = new XMLHttpRequest();
            console.log(payload)
            xhr2.open('POST', `http://localhost:8080/product/report`, true);
            xhr2.setRequestHeader('Content-Type', 'application/json');
            xhr2.onreadystatechange = () => {
                if (xhr2.readyState === 4 && xhr2.status === 200) {
                    let response2 = JSON.parse(xhr2.responseText);
                    if(response2.succrss){
                        alert('Compra finalizada');
                        sessionStorage.removeItem('product');
                        window.location.href = 'home.html';
                    } else {
                        alert(response2.msg);
                    }
                }
            }
            xhr2.send(JSON.stringify(payload));
        }
    };
    xhr.send();
}
getProduct();






