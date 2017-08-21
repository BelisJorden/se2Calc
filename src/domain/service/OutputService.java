package domain.service;

import domain.entity.OutputMessage;


public interface OutputService {

     void publish(OutputMessage outputMessage);
}
