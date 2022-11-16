
(ns site.xgo.pages.main-page.frontend.sections.types.subs
    (:require [re-frame.api :as r]))

(r/reg-sub
  :types/all
  (fn [db [_]]
    (get-in db [:site :types])))

(r/reg-sub
  :types/by-model
  :<- [:types/all]
  :<- [:models.selected/types]
  (fn [[types model-type-ids] [_]]
    (select-keys types model-type-ids)))