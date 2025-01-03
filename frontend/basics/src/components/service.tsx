const baseUrl = process.env.BASE_URL || 'http://localhost:8080';

export interface CustomerData {
    id: string;
    name: string;
    email: string;
}

export interface EditCustomerRequest {
    id: string;
    name: string;
    email: string;
}

export interface AddCustomerRequest {
    name: string;
    email: string;
}

export const createCustomer = (req: AddCustomerRequest) => {
    return fetch(`/api/customers`, {
        method: 'POST',
        headers: {
            'Access-Control-Request-Method': 'POST',
            'Content-type': 'application/json; charset=UTF-8',
        },
        body: JSON.stringify(req),
    });
};

export function getCustomers(
    setCategories: (value: CustomerData[]) => void,
    setLoading?: (value: boolean) => void,
    setError?: (value: boolean) => void
): void {
    if (setLoading) setLoading(true);

    fetch(`/api/customers`,
        {
            headers: {
                "accepts": "application/json"
            }
        })
        .then((_) => _.json())
        .then((customers) => {
            setCategories(customers);
        })
        .catch(setError)
        .finally(() => {
            if (setLoading) setLoading(false);
        });
}

export const updateCustomer = (req: EditCustomerRequest) => {
    return fetch(`/api/customers`, {
        method: 'PUT',
        headers: {
            'Content-type': 'application/json; charset=UTF-8',
        },
        body: JSON.stringify(req),
    });
};

export const deleteCustomer = (id: string) => {
    return fetch(`/api/customers/${id}`, {
        method: 'DELETE',
    });
};
