<template>
  <div class="wrap">
    <div class="loginBox">
      <h2>CrowOj后台管理</h2>
      <el-form
          ref="ruleFormRef"
          :model="loginForm"
          :rules="rules"
          label-width="100px"
          style="max-width: 460px"
      >
        <el-form-item label="账号/邮箱" prop="identifier">
          <el-input v-model="loginForm.identifier" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password"/>
        </el-form-item>
      </el-form>
      <el-button @click="clickLogin(ruleFormRef)" size="large" type="primary">登录</el-button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {reactive, ref} from "vue";
  import {checkPassword, checkUserOrEmail} from "../../utils/check.rule";
  import type { ElForm } from 'element-plus'
import {signIn} from "../../axios/network/user";
import {useStore} from "vuex";
import {useRouter} from "vue-router";
  type FormInstance = InstanceType<typeof ElForm>
  const ruleFormRef = ref<FormInstance>()
  const stores = useStore()
  const router = useRouter()
  const loginForm = reactive({
    identifier:'Xx1871339825',
    password:'Xx123456789'
  })
  const rules = reactive({
    identifier: [{validator:checkUserOrEmail,trigger: 'blur'}],
    password: [{validator:checkPassword,trigger: 'blur'}]
  })


  const clickLogin = (formEl:FormInstance) => {
    formEl.validate((valid) => {
      if (valid) {
        signIn(loginForm).then((res) => {
          const {data} = res.data
          stores.dispatch('setUserStoreAsync',data)
          router.push('/')
        })
      } else {
        console.log('error submit!')
        return false
      }
    })
  }
</script>

<style lang="scss" scoped>
@import "Login.scss";
</style>
