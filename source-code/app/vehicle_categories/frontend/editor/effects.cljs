
(ns app.vehicle-categories.frontend.editor.effects
    (:require [app.vehicle-categories.frontend.editor.views :as editor.views]
              [re-frame.api                                 :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :vehicle-categories.editor/load-editor!
  {:dispatch-n [[:x.gestures/init-view-handler! :vehicle-categories.editor
                                                {:default-view-id :data}]
                [:x.ui/render-surface! :vehicle-categories.editor/view
                                       {:content #'editor.views/view}]]})
