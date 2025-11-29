package net.kingchev.interaction.modal

import net.kingchev.interaction.modal.builder.ExtModalBuilder
import net.kingchev.interaction.modal.data.ModalData
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public abstract class AbstractModal(builder: ExtModalBuilder.() -> Unit) : IModal {
    public val data: ModalData = ExtModalBuilder().apply(builder).build()

    protected companion object {
        public val logger: Logger = LoggerFactory.getLogger(AbstractModal::class.java)
    }
}