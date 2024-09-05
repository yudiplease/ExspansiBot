package dev.yudiplease.exspansi.bot;

import dev.yudiplease.exspansi.bot.service.DiscordService;
import discord4j.core.object.entity.Attachment;
import discord4j.core.spec.EmbedCreateSpec;

public class SendMessageToChannel {
    private final DiscordService discordService;

    public SendMessageToChannel(DiscordService discordService) {
        this.discordService = discordService;
    }

    public void sendMessageToChannel(Long channelId, String message) {
        discordService.sendMessageToChannel(channelId, message).subscribe();
    }

    public void sendMessageToChannelAsEmbed(Long channelId, EmbedCreateSpec spec) {
        discordService.sendMessageToChannelAsEmbed(channelId, spec).subscribe();
    }

    public void sendMessageWithFileToChannel(Long channelId, String message, Attachment file) {

    }
}
