package com.bitplugins.lottery.model

data class EditItemLotteryConfig(
    var title: String = "&7Lottery > &aNew Lottery",
    var pattern: List<String> = listOf(
        "XXXXXXWSS",
        "XQXCXBWSS",
        "XDXMXKWSS",
        "XXXXXXWSS",
        "XXXZXXWSS"
    ),
    var items: Map<String, ItemConfig> = mapOf(
        "on-back" to ItemConfig(
            code = "Z",
            action = "BACK",
            name = "&7Back",
            lore = listOf("&a ", "&7Left/Right-Click to go back"),
            material = "ARROW",
            glow = true
        ),
        "info-lottery" to ItemConfig(
            code = "B",
            action = "INFO_LOTTERY",
            name = "&2Lottery Info:",
            lore = listOf(
                "&a",
                "&7Code: {code}",
                "&7Name: {name}",
                "&7Tickets: {tickets}",
                "&7Price Un: {price}",
                "&7Your profit: {profit}",
                "&7Date: {date}",
                "&7"
            ),
            material = "ENDER_EYE",
            glow = true
        ),
        "item-lottery" to ItemConfig(
            code = "C",
            action = "AWARD",
            name = "&2No Prize Selected",
            lore = listOf(
                "&a ",
                "&a ",
                "&7Left-click to close inventory for select item.",
                "&7Right-click to select in main hand."
            ),
            material = "BARRIER",
            glow = false
        ),
        "ticket-amount" to ItemConfig(
            code = "Q",
            action = "TICKET_AMOUNT",
            name = "&2Number of Shares",
            lore = listOf(
                "&a ",
                "&7Amount: {amount}",
                "&a ",
                "&7Left-click to previous",
                "&7Right-click to next"
            ),
            material = "FEATHER",
            glow = false
        ),
        "ticket-price" to ItemConfig(
            code = "M",
            action = "TICKET_PRICE",
            name = "&2Ticket Price",
            lore = listOf(
                "&a ",
                "&7Price per ticket: {price}",
                "&a ",
                "&7Left/Right-click to set price."
            ),
            material = "SLIME_BALL",
            glow = false
        ),
        "date-lottery" to ItemConfig(
            code = "D",
            action = "DATE_LOTTERY",
            name = "&2Draw Date",
            lore = listOf(
                "&a ",
                "&7Draw date is {date}",
                "&a ",
                "&7Limit of {limit} days.",
                "&a ",
                "&7Right-click to go to previous date",
                "&7Left-click to go to next date"
            ),
            material = "CLOCK",
            glow = false
        ),
        "confirm-lottery" to ItemConfig(
            code = "K",
            action = "CONFIRM_LOTTERY",
            name = "&2Confirm Creation",
            lore = listOf(
                "&a ",
                "&7Left/Right-click to create a lottery"
            ),
            material = "LIME_WOOL",
            glow = false
        ),
        "nothing" to ItemConfig(
            code = "W",
            action = "NOTHING",
            name = "&2",
            lore = listOf("&a "),
            material = "WHITE_STAINED_GLASS_PANE",
            glow = false
        ),
        "inv-see" to ItemConfig(
            code = "S",
            action = "SHULKER_BOX_SEE",
            name = "&2",
            lore = listOf("&a "),
            material = "WHITE_STAINED_GLASS_PANE",
            glow = false
        )
    )
)

data class ItemConfig(
    var code: String,
    var action: String,
    var name: String,
    var lore: List<String>,
    var material: String,
    var glow: Boolean
)