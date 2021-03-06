/*
 * Copyright 2015 Karl Dahlgren
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.castlemock.core.mock.soap.service.project.input;

import com.castlemock.core.basis.model.Input;
import com.castlemock.core.basis.model.SearchQuery;
import com.castlemock.core.basis.model.validation.NotNull;

import java.util.Objects;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
public final class SearchSoapProjectInput implements Input {

    @NotNull
    private final SearchQuery searchQuery;

    private SearchSoapProjectInput(final Builder builder) {
        this.searchQuery = Objects.requireNonNull(builder.searchQuery);
    }

    public SearchQuery getSearchQuery() {
        return searchQuery;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        private SearchQuery searchQuery;

        private Builder(){

        }

        public Builder searchQuery(final SearchQuery searchQuery){
            this.searchQuery = searchQuery;
            return this;
        }

        public SearchSoapProjectInput build(){
            return new SearchSoapProjectInput(this);
        }
    }
}
