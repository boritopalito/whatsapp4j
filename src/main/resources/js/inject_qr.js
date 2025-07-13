(async () => {
    const registrationInfo = await window.AuthStore.RegistrationUtils.waSignalStore.getRegistrationInfo();
    const noiseKeyPair = await window.AuthStore.RegistrationUtils.waNoiseInfo.get();
    const staticKeyB64 = window.AuthStore.Base64Tools.encodeB64(noiseKeyPair.staticKeyPair.pubKey);
    const identityKeyB64 = window.AuthStore.Base64Tools.encodeB64(registrationInfo.identityKeyPair.pubKey);
    const advSecretKey = await window.AuthStore.RegistrationUtils.getADVSecretKey();
    const platform = window.AuthStore.RegistrationUtils.DEVICE_PLATFORM;
    const getQR = (ref) => ref + ',' + staticKeyB64 + ',' + identityKeyB64 + ',' + advSecretKey + ',' + platform;

    if (!window.__myQRInjected) {
        console.log('Injected QR-code JS.');
        window.onQRChangedEvent(getQR(window.AuthStore.Conn.ref));
        window.AuthStore.Conn.on('change:ref', (_, ref) => {
            console.log('QR refresh triggered.')
            window.onQRChangedEvent(getQR(ref));
        });
        window.__myQRInjected = true;
    }

})();
