
(ns site.components.frontend.sidebar.effects
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :site.components/show-sidebar!
  ; @usage
  ;  [:site.components/show-sidebar!]
  (fn [{:keys [db]} _]
      {:db       (assoc-in db [:site.components :sidebar/meta-items :visible?] true)
       :dispatch [:environment/add-scroll-prohibition! ::prohibition]}))

(r/reg-event-fx :site.components/hide-sidebar!
  ; @usage
  ;  [:site.components/hide-sidebar!]
  (fn [{:keys [db]} _]
      {:db       (assoc-in db [:site.components :sidebar/meta-items :visible?] false)
       :dispatch [:environment/enable-scroll!]}))
