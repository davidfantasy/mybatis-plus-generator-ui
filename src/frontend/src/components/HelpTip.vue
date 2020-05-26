<template>
  <el-tooltip :content="content">
    <i class="el-icon-question" style="cursor:hand;"></i>
  </el-tooltip>
</template>
<script>
import axios from "axios";
import _ from "lodash";
export default {
  props: ["content"]
};
</script>