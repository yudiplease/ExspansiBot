package dev.yudiplease.exspansi.bot.service;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Attachment;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class DiscordService {
    private static final Logger logger = LoggerFactory.getLogger(DiscordService.class);

    private final GatewayDiscordClient gateway;

    public DiscordService(String token) {
        this.gateway = DiscordClient.create(token).login().block();
    }

    public Mono<Void> sendMessageToChannel(Long channelId, String message) {
        MessageCreateSpec spec = MessageCreateSpec.builder().content(message).build();
        return gateway.getChannelById(Snowflake.of(channelId))
                .ofType(MessageChannel.class)
                .flatMap(channel -> channel.createMessage(spec))
                .then();
    }

    public Mono<Void> sendMessageToChannelAsEmbed(Long channelId, EmbedCreateSpec embed) {
        MessageCreateSpec spec = MessageCreateSpec.builder().addEmbed(embed).build();
        return gateway.getChannelById(Snowflake.of(channelId))
                .ofType(MessageChannel.class)
                .flatMap(channel -> channel.createMessage(spec))
                .then();
    }
//    public Mono<Void> sendMessageToChannelWithFile(Long channelId, String message, Attachment attachment) {
//        return gateway.getChannelById(Snowflake.of(channelId))
//                .ofType(MessageChannel.class)
//                .flatMap(channel -> channel.createMessage(message -> message.addFile(attachment.getFilename(), )))
//                .then();
//    }

//    public Mono<Void> sendMessageWithFileToChannel(Long channelId, String message, Attachment file) {
//        try {
//            MessageCreateSpec spec = MessageCreateSpec.builder().content(message).addFile(convertAttachmentToFile(file)).build();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return gateway.getChannelById(Snowflake.of(channelId))
//                .ofType(MessageChannel.class)
//                .flatMap(channel -> channel.createMessage(spec -> {
//                    try {
//                        spec.addFile(convertAttachmentToFile(file)).setContent(message);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }).then();
//    }

    private static File convertAttachmentToFile(Attachment attachment) throws IOException {
            URL url = new URL(attachment.getUrl());
            Path tempFile = Files.createTempFile("attachment_", "_" + attachment.getFilename());
            Files.copy(url.openStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
            File file = tempFile.toFile();
            return file;
        }
}
