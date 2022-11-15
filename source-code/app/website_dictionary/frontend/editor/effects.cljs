
(ns app.website-dictionary.frontend.editor.effects
  (:require [app.website-dictionary.frontend.editor.views :as editor.views]
            [re-frame.api                                 :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :website-dictionary.editor/load-editor!
  {:dispatch-n [[:x.gestures/init-view-handler! :website-dictionary.editor
                                                {:default-view-id :dictionary}]
                [:x.ui/render-surface! :website-dictionary.editor/view
                                       {:content #'editor.views/view}]]})
