<template>
  <md-editor v-model="desc" previewOnly />
</template>

<script lang="ts" setup>
import MdEditor from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import {onMounted, ref, watch} from "vue";
import {getCompetitionDesc} from "../../axios/network/competition";

const props = defineProps({
  cid:{
    required:true
  }
})
const desc = ref('')

onMounted(() => {
  getCompetitionDesc(props.cid).then(res => {
    const {data} = res.data
    desc.value = data
  })
})
watch(()=>props.cid,()=>{
  getCompetitionDesc(props.cid).then(res => {
    const {data} = res.data
    desc.value = data
  })
})

</script>

<style scoped lang="scss">

</style>
