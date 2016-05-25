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
package org.jdbi.v3.sqlobject;

import java.lang.reflect.Method;
import java.util.function.Supplier;

import org.jdbi.v3.Handle;
import org.jdbi.v3.TransactionCallback;
import org.jdbi.v3.TransactionIsolationLevel;
import org.jdbi.v3.exceptions.TransactionException;

class PassThroughTransactionHandler implements Handler
{
    private final TransactionIsolationLevel isolation;
    private final PassThroughHandler delegate;

    PassThroughTransactionHandler(Transaction tx)
    {
        this.isolation = tx.value();
        this.delegate = new PassThroughHandler();
    }

    @Override
    public Object invoke(Supplier<Handle> handle, Object target, Object[] args, Method method) throws Exception
    {
        Handle h = handle.get();
        if (h.isInTransaction()) {
            throw new TransactionException("Nested @Transaction detected - this is currently not supported.");
        }

        TransactionCallback<Object, Exception> callback = (conn, status) -> delegate.invoke(handle, target, args, method);

        if (isolation == TransactionIsolationLevel.INVALID_LEVEL) {
            return h.inTransaction(callback);
        }
        else {
            return h.inTransaction(isolation, callback);
        }
    }
}