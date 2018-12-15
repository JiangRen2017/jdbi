/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbi.v3.gson2;

import org.jdbi.v3.json.TestJsonPlugin;
import org.jdbi.v3.postgres.PostgresDbRule;
import org.jdbi.v3.testing.JdbiRule;
import org.junit.Before;
import org.junit.Rule;

public class TestGson2Plugin extends TestJsonPlugin {
    @Rule
    public JdbiRule db = PostgresDbRule.rule();

    @Before
    public void before() {
        setJdbi(db.getJdbi().installPlugin(new Gson2Plugin()));
    }

    @Override
    protected Class<? extends BaseBean> getBeanClass() {
        return BaseBean.class;
    }

    @Override
    protected Class<? extends BaseDao> getDaoClass() {
        return GsonDao.class;
    }

    public interface GsonDao extends BaseDao<BaseDaoSubject> {}
}