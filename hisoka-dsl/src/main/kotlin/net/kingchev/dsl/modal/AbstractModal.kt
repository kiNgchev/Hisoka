package net.kingchev.dsl.modal

import org.slf4j.Logger
import org.slf4j.LoggerFactory

public abstract class AbstractModal(builder: ExtModalBuilder.() -> Unit) : IModal {
    public val data: ModalData = ExtModalBuilder().apply(builder).build()

    protected companion object {
        public val logger: Logger = LoggerFactory.getLogger(AbstractModal::class.java)
    }
}