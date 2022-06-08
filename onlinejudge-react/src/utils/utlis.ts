export function getCompetitionStatus(startDate:Date,endDate:Date){
    let current = new Date().getTime()
    let start = startDate.getTime()
    let end = startDate.getTime()
    if (current < start){
        return 0
    }else if (current>start && current<end){
        return 1
    }else {
        return 2
    }
}
export function getTimeLeft2ByMss(mss:number): string {
    let s = Number((mss/1000).toFixed(0))
    let hours = Math.round((s - 30 * 60) / (60 * 60));
    let minutes = Math.round((s - 30) / 60) % 60;
    let seconds = s % 60;
    return (hours > 0 ? (hours + "小时") : "") + (minutes > 0 ? (minutes + "分钟") : "") + (seconds > 0 ? (seconds + "秒") : "");
}
