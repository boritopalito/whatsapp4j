(async () => {
    console.log("Injecting W4J")

    window.W4J = {};

    window.W4J.getChats = async () => {
        const chats = window.Store.Chat.getModelsArray();
        const chatPromises = chats.map(chat => window.W4J.getChatModel(chat));
        return await Promise.all(chatPromises);
    }

    window.W4J.getChat = async (chatId) => {
        const isChannel = /@\w*newsletter\b/.test(chatId);
        let chatWid;
        try {
            chatWid = window.Store.WidFactory.createWid(chatId);
        } catch (Exception) {
            return null;
        }

        let chat;

        if (isChannel) {
            try {
                chat = window.Store.NewsletterCollection.get(chatId);
                if (!chat) {
                    await window.Store.ChannelUtils.loadNewsletterPreviewChat(chatId);
                    chat = await window.Store.NewsletterCollection.find(chatWid);
                }
            } catch (err) {
                chat = null;
            }
        } else {
            chat = window.Store.Chat.get(chatWid) || (await window.Store.Chat.find(chatWid));
        }

        return window.W4J.getChatModel(chat);
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

    window.W4J.getMessageModel = (message) => {
        const msg = message.serialize();

        msg.id = message.id._serialized;

        msg.isNewMessage = !!msg.isNewMsg;

        if (msg.buttons) {
            msg.buttons = msg.buttons.serialize();
        }

        if (msg.dynamicReplyButtons) {
            msg.dynamicReplyButtons = JSON.parse(JSON.stringify(msg.dynamicReplyButtons));
        }
        if (msg.replyButtons) {
            msg.replyButtons = JSON.parse(JSON.stringify(msg.replyButtons));
        }

        delete msg.pendingAckUpdate;

        console.log(msg);

        return JSON.stringify(msg);
    }
})