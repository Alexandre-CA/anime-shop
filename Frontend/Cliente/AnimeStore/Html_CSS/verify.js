const verifySession = () => {
    return !!sessionStorage.getItem('userData');
}
if (!verifySession()) {
    window.location.href = 'index.html';
}