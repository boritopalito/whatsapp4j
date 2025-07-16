(() => {
    window.Store.Msg.on("add", (msg) => {
        window.onAddMessageEvent(window.W4J.getMessageModel(msg));
    })
})