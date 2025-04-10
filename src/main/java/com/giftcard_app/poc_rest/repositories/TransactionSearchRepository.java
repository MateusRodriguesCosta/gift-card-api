package com.giftcard_app.poc_rest.repositories;

import com.giftcard_app.poc_rest.elastic.TransactionDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface TransactionSearchRepository extends ElasticsearchRepository<TransactionDocument, UUID> {
}
