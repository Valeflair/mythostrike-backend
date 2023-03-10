package com.mythostrike.support;

import com.mythostrike.controller.message.lobby.SeatMessage;

import java.util.List;

public record LobbyData(int id, String mode, String owner, List<SeatMessage> seats) {
}
