package ru.perm.v.transformer;

import org.springframework.stereotype.Component;

import ru.perm.v.dto.MessageDTO;

@Component
public class SuperHeroSearcher {

	public MessageDTO search() {
		return MessageDTO.build();
	}

}
