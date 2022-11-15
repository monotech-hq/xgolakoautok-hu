
(ns site.xgo.pages.main-page.frontend.sections.models.effects
    (:require [re-frame.api :as r]))

(r/reg-event-fx
  :models/select! 
 (fn [_ [_ id]]
    {:dispatch-n [[:x.db/set-item! [:filters :model] id]]}))