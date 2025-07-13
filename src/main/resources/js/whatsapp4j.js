(async () => {
    console.log("Injecting W4J")

    window.W4J = {};

    window.W4J.getChats = async () => {
        const chats = window.Store.Chat.getModelsArray();
        const chatPromises = chats.map(chat => window.W4J.getChatModel(chat));
        return await Promise.all(chatPromises);
    }

    window.W4J.getChatModel = async (chat) => {
        const model = chat.serialize();

        model.id = model.id._serialized;

        model.formattedTitle = chat.formattedTitle;

        model.isGroup = false;
        model.type = 'private';

        model.pinned = !!model.pin;

        model.isMuted = chat.mute?.expiration !== 0;

        model.timestamp = model.t;

        if (chat.groupMetadata) {
            model.isGroup = true;
            model.groupMetadata = chat.groupMetadata.serialize();
            model.type = 'group';
        }

        delete model.msgs;
        delete model.msgUnsyncedButtonReplyMsgs;
        delete model.unsyncedButtonReplies;

        console.log(model);

        return model;
    }
})