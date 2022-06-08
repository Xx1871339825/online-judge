import UserStore from "./user.store";
import ModalStore from "./modal.store";
import CompetitionStore from "./competition.store";
let userStore = new UserStore()
let modalStore = new ModalStore()
let competitionStore = new CompetitionStore()

const stores = {
    userStore,modalStore,competitionStore
}

export default stores
