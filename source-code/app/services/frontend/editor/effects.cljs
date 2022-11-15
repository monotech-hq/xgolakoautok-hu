
(ns app.services.frontend.editor.effects
    (:require [app.services.frontend.editor.views :as editor.views]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :services.editor/load-editor!
  {:dispatch-n [[:x.gestures/init-view-handler! :services.editor
                                                {:default-view-id :data}]
                [:x.ui/render-surface! :services.editor/view
                                       {:content #'editor.views/view}]]})
