package org.wingstudio.blog.vo;

import lombok.Data;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;

@Data
public class TagVo {
    public TagVo(StringTerms.Bucket bucket){
        this.name=bucket.getKey().toString();
        this.count=bucket.getDocCount();
    }
    private String name;
    private Long count;
}
