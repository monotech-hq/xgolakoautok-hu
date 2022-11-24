(ns site.components.frontend.stepper.effects
 (:require [re-frame.api :as r]))

(r/reg-event-db
 :step-next
 (fn [db [_]]
   (update-in db [:stepper] inc)))

(r/reg-event-fx
 :stepper/next!
 (fn [_ [_]]
   {:dispatch [:step-next]}))

(r/reg-event-db
 :step-back
 (fn [db [_]]
   (update-in db [:stepper] dec)))

(r/reg-event-fx
 :stepper/back!
 (fn [_ [_]]
   {:dispatch [:step-back]}))

(r/reg-event-fx
 :stepper/select!
 (fn [_ [_ step-id]]
   {:dispatch [:x.db/set-item! [:stepper] step-id]}))