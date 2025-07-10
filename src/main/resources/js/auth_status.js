async () => {
    let state = window.AuthStore.AppState.state;
    if (state === 'OPENING' || state === 'UNLAUNCHED' || state === 'PAIRING') {
        await new Promise(r => {
            window.AuthStore.AppState.on('change:state', function waitTillInit(_AppState, state) {
                if (state !== 'OPENING' && state !== 'UNLAUNCHED' && state !== 'PAIRING') {
                    window.AuthStore.AppState.off('change:state', waitTillInit);
                    r();
                }
            });
        });
    }
    state = window.AuthStore.AppState.state;
    return state == 'UNPAIRED' || state == 'UNPAIRED_IDLE';
}
