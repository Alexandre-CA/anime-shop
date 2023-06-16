let products = [];


function addCart(add) {
  const sess = JSON.parse(sessionStorage.getItem('product'));
  if (sess) {
    let findOther = false;
    sess.forEach(el => {
      if (el.id === products[add].id) {
        el.qtd += 1;
        sessionStorage.setItem('product', JSON.stringify(sess));
        findOther = true;
        return;
      }
    })
    if(!findOther){
      sessionStorage.setItem('product', JSON.stringify([...sess, products[add]]));
    }
  } else {
    sessionStorage.setItem('product', JSON.stringify([products[add]]));
  }
}
// const setEventListeners = () => {
//   const buttonAdc = document.getElementsByClassName("card-btn");
//   buttonAdc[0].addEventListener('click', function () {
//     console.log('clicou');
//   });
//   // Adicionar um ouvinte de eventos de clique a cada botão
//   for (var i = 0; i < buttonAdc.length; i++) {
//     buttonAdc[i].addEventListener("click", function (event) {
//       const productCard = this.closest('.product-card');
//       const productBrand = productCard.querySelector('.product-brand');
//       const productImage = productCard.querySelector('.image-prod');
//       const productDescription = productCard.querySelector('.product-short-description');
//       const price = productCard.querySelector('.price');
//       const productThumb = productCard.querySelector('.product-thumb');
//       const cload = {
//         brand: productBrand.textContent,
//         description: productDescription.textContent,
//         price: price.textContent,
//         thumbSrc: productThumb.src
//       }
//       console.log(cload);
//       const sess = JSON.parse(sessionStorage.getItem('product'));
//       if (sess) {
//         sessionStorage.setItem('product', JSON.stringify([...sess, cload]));
//       } else {
//         sessionStorage.setItem('product', JSON.stringify([cload]));
//       }

//     });
//   }

// }

const getProducts = () => {

  const decimal = new Intl.NumberFormat('pt-BR', {
    style: 'decimal',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  });

  var xhr = new XMLHttpRequest();

  xhr.open("GET", "http://localhost:8080/product", true);
  
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      var response = JSON.parse(xhr.responseText);
      let productsString = "";
      products = response.data;
      response.data.forEach((el, idx) => {
        productsString += `
        <div class="product-card">
          <div class="product-image">
          ${el.promotion ?
            `<span class="discount-tag">${el.promotion * 100}% OFF</span>`
            : ''
          }
              <img class="image-prod" src="${el.image}" alt="" class="product-thumb">
              <button class="card-btn" onClick="addCart(${idx})">Adicionar no carrinho</button>
          </div>
          <div class="product-info">
              <h2 class="product-brand">${el.name}</h2>
              <p class="product-short-description"> ${el.description}</p>
              <span class="price">R$ ${decimal.format(el.price - (el.price * el.promotion))}</span>
              ${el.promotion ?
            `<span class="actual-price">R$ ${decimal.format(el.price)}</span>`
            : ''
          }
              
          </div>
        </div>`
      })
      document.getElementById('productContainer').innerHTML = productsString;
    }
  };

  xhr.send();
}
getProducts();



