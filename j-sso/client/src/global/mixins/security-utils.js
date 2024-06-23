import store from "@/store";

export function hasAnyAuthority(authorities) {
    let userAuthorities = store.getters.getAuthorities;
    if (!userAuthorities || userAuthorities.length === 0) {
        return false;
    }

    for (let auth of authorities) {
        if (userAuthorities.includes(auth)) {
            return true;
        }
    }
    return false;
}