<template>
  <h1>HOME PAGE</h1>
  <button @click="refresh">
    REFRESH TOKENS
  </button>
  <div>
    <img :src="avatarUrl"/>
  </div>
  <p aria-multiline="true" aria-rowcount="20">
    {{ tokenInfoString }}
  </p>
</template>

<script>

import LoginService from "@/services/login-service";
import axios from "axios";

export default {
  name: "home",
  data: () => {
    return {
      accessToken: null,
      tokenInfo: {}
    }
  },
  methods: {
    getCurrentPrincipal() {
      return LoginService.getTokenInfo()
          .then(result => {
            console.log("Result getting token info: ", result);
            if (!result.data.active) {
              this.$router.replace({name: "login"});
              return;
            }
            this.tokenInfo = result.data;
          })
          .catch((err) => {
            console.log("Error getting token info: ", err);
            this.$router.replace({name: "login"});
          })
    },
    refresh() {
      let refreshToken = window.sessionStorage.getItem("refresh_token");
      if (refreshToken === 'undefined') {
        alert("Не применимо. Refresh токена нет.");
        return;
      }
      return LoginService.refreshToken().then(() => {
        return this.getCurrentPrincipal();
      })
    }
  },
  computed: {
    tokenInfoString() {
      if (!this.tokenInfo) {
        return null;
      }
      return JSON.stringify(this.tokenInfo, null, 8);
    },
    userId() {
      if (this.tokenInfo && this.tokenInfo.principal) {
        return this.tokenInfo.principal.id;
      }
      return null;
    },
    avatarUrl() {
      if (!this.userId) {
        return null;
      }
      return axios.defaults.baseURL + `/resource/user/${this.userId}/avatar?access_token=` + this.accessToken;
    }
  },
  mounted() {
    this.getCurrentPrincipal();
    this.accessToken = window.sessionStorage.getItem("access_token");
  }
}
</script>

<style scoped>
p {
  white-space: pre-wrap;
  text-align: left;
  margin-left: 20px;
  font-size: 1.5em;
}

img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  margin: 10px;
}
</style>